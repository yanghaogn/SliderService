package com.xiaomi.infra.slider.service.client.memcached.client;


import com.xiaomi.infra.slider.service.client.memcached.ClientHandler;
import com.xiaomi.infra.slider.service.common.RequestConfig;
import net.spy.memcached.*;
import net.spy.memcached.internal.BulkFuture;
import net.spy.memcached.internal.GetFuture;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.transcoders.Transcoder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yang on 14-12-26.
 */
public class SliderMemcachedClient implements MemcachedClientIF, ConnectionObserver {
  static Logger log = Logger.getLogger(SliderMemcachedClient.class);
  final String clusterName;
  private volatile MemcachedClient memcachedClient;
  private volatile boolean initted = true;
  private ReentrantLock lock = new ReentrantLock();

  public SliderMemcachedClient(String clusterName, InetSocketAddress address) throws IOException {
    memcachedClient = new MemcachedClient(address);
    this.clusterName = clusterName;
  }

  public SliderMemcachedClient(String clusterName, List<InetSocketAddress> addressList) throws IOException {
    memcachedClient = new MemcachedClient(addressList);
    this.clusterName = clusterName;
  }

  public SliderMemcachedClient(String clusterName) throws IOException {
    this.clusterName = clusterName;
    init();
  }

  private MemcachedClient getMemcachedClient() {
    if (requireInit()) {
      init();
    }
    return memcachedClient;
  }

  public void init() {
    initted = false;
    lock.lock();
    try {
      if (!initted) {
        if (memcachedClient != null) {
          memcachedClient.shutdown();
        }
        String user = System.getProperty(RequestConfig.USER);
        List<String> hostPorts = new ClientHandler().getHostPortList(user, clusterName);
        this.memcachedClient = new MemcachedClient(AddrUtil.getAddresses(hostPorts));
      }
    } catch (IOException e) {
      log.error(e);
    } finally {
      initted = true;
      lock.unlock();
    }
  }

  private boolean requireInit() {
    //memcachedClient.getUnavailableServers().size()启动时，其数目是addressList的大小，之后为０
    return initted = false || memcachedClient == null;

  }

  /**
   * Get the addresses of available servers.
   * <p/>
   * <p>
   * This is based on a snapshot in time so shouldn't be considered completely
   * accurate, but is a useful for getting a feel for what's working and what's
   * not working.
   * </p>
   *
   * @return point-in-time view of currently available servers
   */
  @Override
  public Collection<SocketAddress> getAvailableServers() {
    try {
      return getMemcachedClient().getAvailableServers();
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getAvailableServers();
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getAvailableServers();
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getAvailableServers();
    }
  }

  /**
   * Get the addresses of unavailable servers.
   * <p/>
   * <p>
   * This is based on a snapshot in time so shouldn't be considered completely
   * accurate, but is a useful for getting a feel for what's working and what's
   * not working.
   * </p>
   *
   * @return point-in-time view of currently available servers
   */
  @Override
  public Collection<SocketAddress> getUnavailableServers() {
    try {
      return getMemcachedClient().getUnavailableServers();
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getUnavailableServers();
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getUnavailableServers();
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getUnavailableServers();
    }
  }

  /**
   * Get a read-only wrapper around the node locator wrapping this instance.
   *
   * @return this instance's NodeLocator
   */
  @Override
  public NodeLocator getNodeLocator() {
    try {
      return getMemcachedClient().getNodeLocator();
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getNodeLocator();
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getNodeLocator();
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getNodeLocator();
    }
  }

  /**
   * Get the default transcoder that's in use.
   *
   * @return this instance's Transcoder
   */
  @Override
  public Transcoder<Object> getTranscoder() {
    try {
      return getMemcachedClient().getTranscoder();
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getTranscoder();
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getTranscoder();
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getTranscoder();
    }
  }

  @Override
  public CountDownLatch broadcastOp(final BroadcastOpFactory of) {
    try {
      return getMemcachedClient().broadcastOp(of);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().broadcastOp(of);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().broadcastOp(of);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().broadcastOp(of);
    }
  }

  @Override
  public CountDownLatch broadcastOp(final BroadcastOpFactory of, Collection<MemcachedNode> nodes) {
    try {
      return getMemcachedClient().broadcastOp(of, nodes);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().broadcastOp(of, nodes);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().broadcastOp(of, nodes);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().broadcastOp(of, nodes);
    }
  }

  /**
   * Touch the given key to reset its expiration time with the default
   * transcoder.
   *
   * @param key the key to fetch
   * @param exp the new expiration to set for the given key
   * @return a future that will hold the return value of whether or not the
   * fetch succeeded
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<Boolean> touch(final String key, final int exp) {
    try {
      return getMemcachedClient().touch(key, exp);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().touch(key, exp);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().touch(key, exp);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().touch(key, exp);
    }
  }

  /**
   * Touch the given key to reset its expiration time.
   *
   * @param key the key to fetch
   * @param exp the new expiration to set for the given key
   * @param tc  the transcoder to serialize and unserialize value
   * @return a future that will hold the return value of whether or not the
   * fetch succeeded
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<Boolean> touch(final String key, final int exp, final Transcoder<T> tc) {
    try {
      return getMemcachedClient().touch(key, exp, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().touch(key, exp, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().touch(key, exp, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().touch(key, exp, tc);
    }
  }

  /**
   * Append to an existing value in the cache.
   * <p/>
   * If 0 is passed in as the CAS identifier, it will override the value
   * on the server without performing the CAS check.
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   *
   * @param cas cas identifier (ignored in the ascii protocol)
   * @param key the key to whose value will be appended
   * @param val the value to append
   * @return a future indicating success, false if there was no change to the
   * value
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Boolean> append(long cas, String key, Object val) {
    try {
      return getMemcachedClient().append(cas, key, val);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().append(cas, key, val);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().append(cas, key, val);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().append(cas, key, val);
    }
  }

  /**
   * Append to an existing value in the cache.
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   *
   * @param key the key to whose value will be appended
   * @param val the value to append
   * @return a future indicating success, false if there was no change to the
   * value
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Boolean> append(String key, Object val) {
    try {
      return getMemcachedClient().append(key, val);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().append(key, val);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().append(key, val);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().append(key, val);
    }
  }

  /**
   * Append to an existing value in the cache.
   * <p/>
   * If 0 is passed in as the CAS identifier, it will override the value
   * on the server without performing the CAS check.
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   *
   * @param <T>
   * @param cas cas identifier (ignored in the ascii protocol)
   * @param key the key to whose value will be appended
   * @param val the value to append
   * @param tc  the transcoder to serialize and unserialize the value
   * @return a future indicating success
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<Boolean> append(long cas, String key, T val, Transcoder<T> tc) {
    try {
      return getMemcachedClient().append(cas, key, val, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().append(cas, key, val, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().append(cas, key, val, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().append(cas, key, val, tc);
    }
  }

  /**
   * Append to an existing value in the cache.
   * <p/>
   * If 0 is passed in as the CAS identifier, it will override the value
   * on the server without performing the CAS check.
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   *
   * @param <T>
   * @param key the key to whose value will be appended
   * @param val the value to append
   * @param tc  the transcoder to serialize and unserialize the value
   * @return a future indicating success
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<Boolean> append(String key, T val, Transcoder<T> tc) {
    try {
      return getMemcachedClient().append(key, val, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().append(key, val, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().append(key, val, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().append(key, val, tc);
    }
  }

  /**
   * Prepend to an existing value in the cache.
   * <p/>
   * If 0 is passed in as the CAS identifier, it will override the value
   * on the server without performing the CAS check.
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   *
   * @param cas cas identifier (ignored in the ascii protocol)
   * @param key the key to whose value will be prepended
   * @param val the value to append
   * @return a future indicating success
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Boolean> prepend(long cas, String key, Object val) {
    try {
      return getMemcachedClient().prepend(cas, key, val);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().prepend(cas, key, val);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().prepend(cas, key, val);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().prepend(cas, key, val);
    }
  }

  /**
   * Prepend to an existing value in the cache.
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   *
   * @param key the key to whose value will be prepended
   * @param val the value to append
   * @return a future indicating success
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Boolean> prepend(String key, Object val) {
    try {
      return getMemcachedClient().prepend(key, val);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().prepend(key, val);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().prepend(key, val);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().prepend(key, val);
    }
  }

  /**
   * Prepend to an existing value in the cache.
   * <p/>
   * If 0 is passed in as the CAS identifier, it will override the value
   * on the server without performing the CAS check.
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   *
   * @param <T>
   * @param cas cas identifier (ignored in the ascii protocol)
   * @param key the key to whose value will be prepended
   * @param val the value to append
   * @param tc  the transcoder to serialize and unserialize the value
   * @return a future indicating success
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<Boolean> prepend(long cas, String key, T val, Transcoder<T> tc) {
    try {
      return getMemcachedClient().prepend(cas, key, val, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().prepend(cas, key, val, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().prepend(cas, key, val, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().prepend(cas, key, val, tc);
    }
  }

  /**
   * Prepend to an existing value in the cache.
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   *
   * @param <T>
   * @param key the key to whose value will be prepended
   * @param val the value to append
   * @param tc  the transcoder to serialize and unserialize the value
   * @return a future indicating success
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<Boolean> prepend(String key, T val, Transcoder<T> tc) {
    try {
      return getMemcachedClient().prepend(key, val, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().prepend(key, val, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().prepend(key, val, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().prepend(key, val, tc);
    }
  }

  /**
   * Asynchronous CAS operation.
   *
   * @param <T>
   * @param key   the key
   * @param casId the CAS identifier (from a gets operation)
   * @param value the new value
   * @param tc    the transcoder to serialize and unserialize the value
   * @return a future that will indicate the status of the CAS
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<CASResponse> asyncCAS(String key, long casId, T value, Transcoder<T> tc) {
    try {
      return getMemcachedClient().asyncCAS(key, casId, value, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncCAS(key, casId, value, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncCAS(key, casId, value, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncCAS(key, casId, value, tc);
    }
  }

  /**
   * Asynchronous CAS operation.
   *
   * @param <T>
   * @param key   the key
   * @param casId the CAS identifier (from a gets operation)
   * @param exp   the expiration of this object
   * @param value the new value
   * @param tc    the transcoder to serialize and unserialize the value
   * @return a future that will indicate the status of the CAS
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<CASResponse> asyncCAS(String key, long casId, int exp, T value, Transcoder<T> tc) {
    try {
      return getMemcachedClient().asyncCAS(key, casId, exp, value, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncCAS(key, casId, exp, value, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncCAS(key, casId, exp, value, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncCAS(key, casId, exp, value, tc);
    }
  }

  /**
   * Asynchronous CAS operation using the default transcoder.
   *
   * @param key   the key
   * @param casId the CAS identifier (from a gets operation)
   * @param value the new value
   * @return a future that will indicate the status of the CAS
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<CASResponse> asyncCAS(String key, long casId, Object value) {
    try {
      return getMemcachedClient().asyncCAS(key, casId, value);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncCAS(key, casId, value);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncCAS(key, casId, value);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncCAS(key, casId, value);
    }
  }

  /**
   * Asynchronous CAS operation using the default transcoder with expiration.
   *
   * @param key   the key
   * @param casId the CAS identifier (from a gets operation)
   * @param exp   the expiration of this object
   * @param value the new value
   * @return a future that will indicate the status of the CAS
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<CASResponse> asyncCAS(String key, long casId, int exp, Object value) {
    try {
      return getMemcachedClient().asyncCAS(key, casId, exp, value);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncCAS(key, casId, exp, value);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncCAS(key, casId, exp, value);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncCAS(key, casId, exp, value);
    }
  }

  /**
   * Perform a synchronous CAS operation.
   *
   * @param <T>
   * @param key   the key
   * @param casId the CAS identifier (from a gets operation)
   * @param value the new value
   * @param tc    the transcoder to serialize and unserialize the value
   * @return a CASResponse
   * @throws net.spy.memcached.OperationTimeoutException if global operation timeout is exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public <T> CASResponse cas(String key, long casId, T value, Transcoder<T> tc) {
    try {
      return getMemcachedClient().cas(key, casId, value, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().cas(key, casId, value, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().cas(key, casId, value, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().cas(key, casId, value, tc);
    }
  }

  /**
   * Perform a synchronous CAS operation.
   *
   * @param <T>
   * @param key   the key
   * @param casId the CAS identifier (from a gets operation)
   * @param exp   the expiration of this object
   * @param value the new value
   * @param tc    the transcoder to serialize and unserialize the value
   * @return a CASResponse
   * @throws net.spy.memcached.OperationTimeoutException if global operation timeout is exceeded
   * @throws java.util.concurrent.CancellationException  if operation was canceled
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public <T> CASResponse cas(String key, long casId, int exp, T value, Transcoder<T> tc) {
    try {
      return getMemcachedClient().cas(key, casId, exp, value, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().cas(key, casId, exp, value, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().cas(key, casId, exp, value, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().cas(key, casId, exp, value, tc);
    }
  }

  /**
   * Perform a synchronous CAS operation with the default transcoder.
   *
   * @param key   the key
   * @param casId the CAS identifier (from a gets operation)
   * @param value the new value
   * @return a CASResponse
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public CASResponse cas(String key, long casId, Object value) {
    try {
      return getMemcachedClient().cas(key, casId, value);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().cas(key, casId, value);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().cas(key, casId, value);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().cas(key, casId, value);
    }
  }

  /**
   * Perform a synchronous CAS operation with the default transcoder.
   *
   * @param key   the key
   * @param casId the CAS identifier (from a gets operation)
   * @param exp   the expiration of this object
   * @param value the new value
   * @return a CASResponse
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public CASResponse cas(String key, long casId, int exp, Object value) {
    try {
      return getMemcachedClient().cas(key, casId, exp, value);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().cas(key, casId, exp, value);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().cas(key, casId, exp, value);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().cas(key, casId, exp, value);
    }
  }

  /**
   * Add an object to the cache iff it does not exist already.
   * <p/>
   * <p>
   * The {@code exp} value is passed along to memcached exactly as given,
   * and will be processed per the memcached protocol specification:
   * </p>
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   * <p/>
   * <blockquote>
   * <p>
   * The actual value sent may either be Unix time (number of seconds since
   * January 1, 1970, as a 32-bit value), or a number of seconds starting from
   * current time. In the latter case, this number of seconds may not exceed
   * 60*60*24*30 (number of seconds in 30 days); if the number sent by a client
   * is larger than that, the server will consider it to be real Unix time value
   * rather than an offset from current time.
   * </p>
   * </blockquote>
   *
   * @param <T>
   * @param key the key under which this object should be added.
   * @param exp the expiration of this object
   * @param o   the object to store
   * @param tc  the transcoder to serialize and unserialize the value
   * @return a future representing the processing of this operation
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<Boolean> add(String key, int exp, T o, Transcoder<T> tc) {
    try {
      return getMemcachedClient().add(key, exp, o, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().add(key, exp, o, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().add(key, exp, o, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().add(key, exp, o, tc);
    }
  }

  /**
   * Add an object to the cache (using the default transcoder) iff it does not
   * exist already.
   * <p/>
   * <p>
   * The {@code exp} value is passed along to memcached exactly as given,
   * and will be processed per the memcached protocol specification:
   * </p>
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   * <p/>
   * <blockquote>
   * <p>
   * The actual value sent may either be Unix time (number of seconds since
   * January 1, 1970, as a 32-bit value), or a number of seconds starting from
   * current time. In the latter case, this number of seconds may not exceed
   * 60*60*24*30 (number of seconds in 30 days); if the number sent by a client
   * is larger than that, the server will consider it to be real Unix time value
   * rather than an offset from current time.
   * </p>
   * </blockquote>
   *
   * @param key the key under which this object should be added.
   * @param exp the expiration of this object
   * @param o   the object to store
   * @return a future representing the processing of this operation
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Boolean> add(String key, int exp, Object o) {
    try {
      return getMemcachedClient().add(key, exp, o);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().add(key, exp, o);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().add(key, exp, o);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().add(key, exp, o);
    }
  }

  /**
   * Set an object in the cache regardless of any existing value.
   * <p/>
   * <p>
   * The {@code exp} value is passed along to memcached exactly as given,
   * and will be processed per the memcached protocol specification:
   * </p>
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   * <p/>
   * <blockquote>
   * <p>
   * The actual value sent may either be Unix time (number of seconds since
   * January 1, 1970, as a 32-bit value), or a number of seconds starting from
   * current time. In the latter case, this number of seconds may not exceed
   * 60*60*24*30 (number of seconds in 30 days); if the number sent by a client
   * is larger than that, the server will consider it to be real Unix time value
   * rather than an offset from current time.
   * </p>
   * </blockquote>
   *
   * @param <T>
   * @param key the key under which this object should be added.
   * @param exp the expiration of this object
   * @param o   the object to store
   * @param tc  the transcoder to serialize and unserialize the value
   * @return a future representing the processing of this operation
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<Boolean> set(String key, int exp, T o, Transcoder<T> tc) {
    try {
      return getMemcachedClient().set(key, exp, o, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().set(key, exp, o, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().set(key, exp, o, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().set(key, exp, o, tc);
    }
  }

  /**
   * Set an object in the cache (using the default transcoder) regardless of any
   * existing value.
   * <p/>
   * <p>
   * The {@code exp} value is passed along to memcached exactly as given,
   * and will be processed per the memcached protocol specification:
   * </p>
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   * <p/>
   * <blockquote>
   * <p>
   * The actual value sent may either be Unix time (number of seconds since
   * January 1, 1970, as a 32-bit value), or a number of seconds starting from
   * current time. In the latter case, this number of seconds may not exceed
   * 60*60*24*30 (number of seconds in 30 days); if the number sent by a client
   * is larger than that, the server will consider it to be real Unix time value
   * rather than an offset from current time.
   * </p>
   * </blockquote>
   *
   * @param key the key under which this object should be added.
   * @param exp the expiration of this object
   * @param o   the object to store
   * @return a future representing the processing of this operation
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Boolean> set(String key, int exp, Object o) {
    try {
      return getMemcachedClient().set(key, exp, o);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().set(key, exp, o);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().set(key, exp, o);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().set(key, exp, o);
    }
  }

  /**
   * Replace an object with the given value iff there is already a value for the
   * given key.
   * <p/>
   * <p>
   * The {@code exp} value is passed along to memcached exactly as given,
   * and will be processed per the memcached protocol specification:
   * </p>
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   * <p/>
   * <blockquote>
   * <p>
   * The actual value sent may either be Unix time (number of seconds since
   * January 1, 1970, as a 32-bit value), or a number of seconds starting from
   * current time. In the latter case, this number of seconds may not exceed
   * 60*60*24*30 (number of seconds in 30 days); if the number sent by a client
   * is larger than that, the server will consider it to be real Unix time value
   * rather than an offset from current time.
   * </p>
   * </blockquote>
   *
   * @param <T>
   * @param key the key under which this object should be added.
   * @param exp the expiration of this object
   * @param o   the object to store
   * @param tc  the transcoder to serialize and unserialize the value
   * @return a future representing the processing of this operation
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<Boolean> replace(String key, int exp, T o, Transcoder<T> tc) {
    try {
      return getMemcachedClient().replace(key, exp, o, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().replace(key, exp, o, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().replace(key, exp, o, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().replace(key, exp, o, tc);
    }
  }

  /**
   * Replace an object with the given value (transcoded with the default
   * transcoder) iff there is already a value for the given key.
   * <p/>
   * <p>
   * The {@code exp} value is passed along to memcached exactly as given,
   * and will be processed per the memcached protocol specification:
   * </p>
   * <p/>
   * <p>
   * Note that the return will be false any time a mutation has not occurred.
   * </p>
   * <p/>
   * <blockquote>
   * <p>
   * The actual value sent may either be Unix time (number of seconds since
   * January 1, 1970, as a 32-bit value), or a number of seconds starting from
   * current time. In the latter case, this number of seconds may not exceed
   * 60*60*24*30 (number of seconds in 30 days); if the number sent by a client
   * is larger than that, the server will consider it to be real Unix time value
   * rather than an offset from current time.
   * </p>
   * </blockquote>
   *
   * @param key the key under which this object should be added.
   * @param exp the expiration of this object
   * @param o   the object to store
   * @return a future representing the processing of this operation
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Boolean> replace(String key, int exp, Object o) {
    try {
      return getMemcachedClient().replace(key, exp, o);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().replace(key, exp, o);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().replace(key, exp, o);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().replace(key, exp, o);
    }
  }

  /**
   * Get the given key asynchronously.
   *
   * @param <T>
   * @param key the key to fetch
   * @param tc  the transcoder to serialize and unserialize value
   * @return a future that will hold the return value of the fetch
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> GetFuture<T> asyncGet(final String key, final Transcoder<T> tc) {
    try {
      return getMemcachedClient().asyncGet(key, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGet(key, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGet(key, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGet(key, tc);
    }
  }

  /**
   * Get the given key asynchronously and decode with the default transcoder.
   *
   * @param key the key to fetch
   * @return a future that will hold the return value of the fetch
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public GetFuture<Object> asyncGet(final String key) {
    try {
      return getMemcachedClient().asyncGet(key);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGet(key);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGet(key);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGet(key);
    }
  }

  /**
   * Gets (with CAS support) the given key asynchronously.
   *
   * @param <T>
   * @param key the key to fetch
   * @param tc  the transcoder to serialize and unserialize value
   * @return a future that will hold the return value of the fetch
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<CASValue<T>> asyncGets(final String key, final Transcoder<T> tc) {
    try {
      return getMemcachedClient().asyncGets(key, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGets(key, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGets(key, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGets(key, tc);
    }
  }

  /**
   * Gets (with CAS support) the given key asynchronously and decode using the
   * default transcoder.
   *
   * @param key the key to fetch
   * @return a future that will hold the return value of the fetch
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<CASValue<Object>> asyncGets(final String key) {
    try {
      return getMemcachedClient().asyncGets(key);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGets(key);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGets(key);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGets(key);
    }
  }

  /**
   * Gets (with CAS support) with a single key.
   *
   * @param <T>
   * @param key the key to get
   * @param tc  the transcoder to serialize and unserialize value
   * @return the result from the cache and CAS id (null if there is none)
   * @throws net.spy.memcached.OperationTimeoutException if global operation timeout is exceeded
   * @throws java.util.concurrent.CancellationException  if operation was canceled
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public <T> CASValue<T> gets(String key, Transcoder<T> tc) {
    try {
      return getMemcachedClient().gets(key, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().gets(key, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().gets(key, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().gets(key, tc);
    }
  }

  /**
   * Get with a single key and reset its expiration.
   *
   * @param <T>
   * @param key the key to get
   * @param exp the new expiration for the key
   * @param tc  the transcoder to serialize and unserialize value
   * @return the result from the cache (null if there is none)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws java.util.concurrent.CancellationException  if operation was canceled
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public <T> CASValue<T> getAndTouch(String key, int exp, Transcoder<T> tc) {
    try {
      return getMemcachedClient().getAndTouch(key, exp, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getAndTouch(key, exp, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getAndTouch(key, exp, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getAndTouch(key, exp, tc);
    }
  }

  /**
   * Get a single key and reset its expiration using the default transcoder.
   *
   * @param key the key to get
   * @param exp the new expiration for the key
   * @return the result from the cache and CAS id (null if there is none)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public CASValue<Object> getAndTouch(String key, int exp) {
    try {
      return getMemcachedClient().getAndTouch(key, exp);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getAndTouch(key, exp);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getAndTouch(key, exp);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getAndTouch(key, exp);
    }
  }

  /**
   * Gets (with CAS support) with a single key using the default transcoder.
   *
   * @param key the key to get
   * @return the result from the cache and CAS id (null if there is none)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public CASValue<Object> gets(String key) {
    try {
      return getMemcachedClient().gets(key);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().gets(key);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().gets(key);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().gets(key);
    }
  }

  /**
   * Get with a single key.
   *
   * @param <T>
   * @param key the key to get
   * @param tc  the transcoder to serialize and unserialize value
   * @return the result from the cache (null if there is none)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws java.util.concurrent.CancellationException  if operation was canceled
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public <T> T get(String key, Transcoder<T> tc) {
    try {
      return getMemcachedClient().get(key, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().get(key, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().get(key, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().get(key, tc);
    }
  }

  /**
   * Get with a single key and decode using the default transcoder.
   *
   * @param key the key to get
   * @return the result from the cache (null if there is none)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public Object get(String key) {
    try {
      return getMemcachedClient().get(key);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().get(key);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().get(key);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().get(key);
    }
  }

  /**
   * Asynchronously get a bunch of objects from the cache.
   *
   * @param <T>
   * @param keyIter Iterator that produces keys.
   * @param tcIter  an iterator of transcoders to serialize and unserialize
   *                values; the transcoders are matched with the keys in the same
   *                order. The minimum of the key collection length and number of
   *                transcoders is used and no exception is thrown if they do not
   *                match
   * @return a Future result of that fetch
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> BulkFuture<Map<String, T>> asyncGetBulk(Iterator<String> keyIter, Iterator<Transcoder<T>> tcIter) {
    try {
      return getMemcachedClient().asyncGetBulk(keyIter, tcIter);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keyIter, tcIter);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keyIter, tcIter);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keyIter, tcIter);
    }
  }

  /**
   * Asynchronously get a bunch of objects from the cache.
   *
   * @param <T>
   * @param keys   the keys to request
   * @param tcIter an iterator of transcoders to serialize and unserialize
   *               values; the transcoders are matched with the keys in the same
   *               order. The minimum of the key collection length and number of
   *               transcoders is used and no exception is thrown if they do not
   *               match
   * @return a Future result of that fetch
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> BulkFuture<Map<String, T>> asyncGetBulk(Collection<String> keys, Iterator<Transcoder<T>> tcIter) {
    try {
      return getMemcachedClient().asyncGetBulk(keys, tcIter);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keys, tcIter);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keys, tcIter);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keys, tcIter);
    }
  }

  /**
   * Asynchronously get a bunch of objects from the cache.
   *
   * @param <T>
   * @param keyIter Iterator for the keys to request
   * @param tc      the transcoder to serialize and unserialize values
   * @return a Future result of that fetch
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> BulkFuture<Map<String, T>> asyncGetBulk(Iterator<String> keyIter, Transcoder<T> tc) {
    try {
      return getMemcachedClient().asyncGetBulk(keyIter, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keyIter, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keyIter, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keyIter, tc);
    }
  }

  /**
   * Asynchronously get a bunch of objects from the cache.
   *
   * @param <T>
   * @param keys the keys to request
   * @param tc   the transcoder to serialize and unserialize values
   * @return a Future result of that fetch
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> BulkFuture<Map<String, T>> asyncGetBulk(Collection<String> keys, Transcoder<T> tc) {
    try {
      return getMemcachedClient().asyncGetBulk(keys, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keys, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keys, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keys, tc);
    }
  }

  /**
   * Asynchronously get a bunch of objects from the cache and decode them with
   * the given transcoder.
   *
   * @param keyIter Iterator that produces the keys to request
   * @return a Future result of that fetch
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public BulkFuture<Map<String, Object>> asyncGetBulk(Iterator<String> keyIter) {
    try {
      return getMemcachedClient().asyncGetBulk(keyIter);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keyIter);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keyIter);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keyIter);
    }
  }

  /**
   * Asynchronously get a bunch of objects from the cache and decode them with
   * the given transcoder.
   *
   * @param keys the keys to request
   * @return a Future result of that fetch
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public BulkFuture<Map<String, Object>> asyncGetBulk(Collection<String> keys) {
    try {
      return getMemcachedClient().asyncGetBulk(keys);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keys);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keys);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keys);
    }
  }

  /**
   * Varargs wrapper for asynchronous bulk gets.
   *
   * @param <T>
   * @param tc   the transcoder to serialize and unserialize value
   * @param keys one more more keys to get
   * @return the future values of those keys
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> BulkFuture<Map<String, T>> asyncGetBulk(Transcoder<T> tc, String... keys) {
    try {
      return getMemcachedClient().asyncGetBulk(tc, keys);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(tc, keys);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(tc, keys);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(tc, keys);
    }
  }

  /**
   * Varargs wrapper for asynchronous bulk gets with the default transcoder.
   *
   * @param keys one more more keys to get
   * @return the future values of those keys
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public BulkFuture<Map<String, Object>> asyncGetBulk(String... keys) {
    try {
      return getMemcachedClient().asyncGetBulk(keys);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keys);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keys);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetBulk(keys);
    }
  }

  /**
   * Get the given key to reset its expiration time.
   *
   * @param key the key to fetch
   * @param exp the new expiration to set for the given key
   * @return a future that will hold the return value of the fetch
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<CASValue<Object>> asyncGetAndTouch(final String key, final int exp) {
    try {
      return getMemcachedClient().asyncGetAndTouch(key, exp);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetAndTouch(key, exp);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetAndTouch(key, exp);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetAndTouch(key, exp);
    }
  }

  /**
   * Get the given key to reset its expiration time.
   *
   * @param key the key to fetch
   * @param exp the new expiration to set for the given key
   * @param tc  the transcoder to serialize and unserialize value
   * @return a future that will hold the return value of the fetch
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public <T> OperationFuture<CASValue<T>> asyncGetAndTouch(final String key, final int exp, final Transcoder<T> tc) {
    try {
      return getMemcachedClient().asyncGetAndTouch(key, exp, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetAndTouch(key, exp, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetAndTouch(key, exp, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncGetAndTouch(key, exp, tc);
    }
  }

  /**
   * Get the values for multiple keys from the cache.
   *
   * @param <T>
   * @param keyIter Iterator that produces the keys
   * @param tc      the transcoder to serialize and unserialize value
   * @return a map of the values (for each value that exists)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws java.util.concurrent.CancellationException  if operation was canceled
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public <T> Map<String, T> getBulk(Iterator<String> keyIter, Transcoder<T> tc) {
    try {
      return getMemcachedClient().getBulk(keyIter, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keyIter, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keyIter, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keyIter, tc);
    }
  }

  /**
   * Get the values for multiple keys from the cache.
   *
   * @param keyIter Iterator that produces the keys
   * @return a map of the values (for each value that exists)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public Map<String, Object> getBulk(Iterator<String> keyIter) {
    try {
      return getMemcachedClient().getBulk(keyIter);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keyIter);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keyIter);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keyIter);
    }
  }

  /**
   * Get the values for multiple keys from the cache.
   *
   * @param <T>
   * @param keys the keys
   * @param tc   the transcoder to serialize and unserialize value
   * @return a map of the values (for each value that exists)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public <T> Map<String, T> getBulk(Collection<String> keys, Transcoder<T> tc) {
    try {
      return getMemcachedClient().getBulk(keys, tc);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keys, tc);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keys, tc);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keys, tc);
    }
  }

  /**
   * Get the values for multiple keys from the cache.
   *
   * @param keys the keys
   * @return a map of the values (for each value that exists)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public Map<String, Object> getBulk(Collection<String> keys) {
    try {
      return getMemcachedClient().getBulk(keys);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keys);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keys);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keys);
    }
  }

  /**
   * Get the values for multiple keys from the cache.
   *
   * @param <T>
   * @param tc   the transcoder to serialize and unserialize value
   * @param keys the keys
   * @return a map of the values (for each value that exists)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public <T> Map<String, T> getBulk(Transcoder<T> tc, String... keys) {
    try {
      return getMemcachedClient().getBulk(tc, keys);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(tc, keys);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(tc, keys);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(tc, keys);
    }
  }

  /**
   * Get the values for multiple keys from the cache.
   *
   * @param keys the keys
   * @return a map of the values (for each value that exists)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public Map<String, Object> getBulk(String... keys) {
    try {
      return getMemcachedClient().getBulk(keys);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keys);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keys);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getBulk(keys);
    }
  }

  /**
   * Get the versions of all of the connected memcacheds.
   *
   * @return a Map of SocketAddress to String for connected servers
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public Map<SocketAddress, String> getVersions() {
    try {
      return getMemcachedClient().getVersions();
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getVersions();
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getVersions();
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getVersions();
    }
  }

  /**
   * Get all of the stats from all of the connections.
   *
   * @return a Map of a Map of stats replies by SocketAddress
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public Map<SocketAddress, Map<String, String>> getStats() {
    try {
      return getMemcachedClient().getStats();
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getStats();
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getStats();
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getStats();
    }
  }

  /**
   * Get a set of stats from all connections.
   *
   * @param arg which stats to get
   * @return a Map of the server SocketAddress to a map of String stat keys to
   * String stat values.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public Map<SocketAddress, Map<String, String>> getStats(final String arg) {
    try {
      return getMemcachedClient().getStats(arg);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getStats(arg);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getStats(arg);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().getStats(arg);
    }
  }

  /**
   * Increment the given key by the given amount.
   * <p/>
   * Due to the way the memcached server operates on items, incremented and
   * decremented items will be returned as Strings with any operations that
   * return a value.
   *
   * @param key the key
   * @param by  the amount to increment
   * @return the new value (-1 if the key doesn't exist)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public long incr(String key, long by) {
    try {
      return getMemcachedClient().incr(key, by);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by);
    }
  }

  /**
   * Increment the given key by the given amount.
   * <p/>
   * Due to the way the memcached server operates on items, incremented and
   * decremented items will be returned as Strings with any operations that
   * return a value.
   *
   * @param key the key
   * @param by  the amount to increment
   * @return the new value (-1 if the key doesn't exist)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public long incr(String key, int by) {
    try {
      return getMemcachedClient().incr(key, by);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by);
    }
  }

  /**
   * Decrement the given key by the given value.
   * <p/>
   * Due to the way the memcached server operates on items, incremented and
   * decremented items will be returned as Strings with any operations that
   * return a value.
   *
   * @param key the key
   * @param by  the value
   * @return the new value (-1 if the key doesn't exist)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public long decr(String key, long by) {
    try {
      return getMemcachedClient().decr(key, by);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by);
    }
  }

  /**
   * Decrement the given key by the given value.
   * <p/>
   * Due to the way the memcached server operates on items, incremented and
   * decremented items will be returned as Strings with any operations that
   * return a value.
   *
   * @param key the key
   * @param by  the value
   * @return the new value (-1 if the key doesn't exist)
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public long decr(String key, int by) {
    try {
      return getMemcachedClient().decr(key, by);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by);
    }
  }

  /**
   * Increment the given counter, returning the new value.
   * <p/>
   * Due to the way the memcached server operates on items, incremented and
   * decremented items will be returned as Strings with any operations that
   * return a value.
   *
   * @param key the key
   * @param by  the amount to increment
   * @param def the default value (if the counter does not exist)
   * @param exp the expiration of this object
   * @return the new value, or -1 if we were unable to increment or add
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public long incr(String key, long by, long def, int exp) {
    try {
      return getMemcachedClient().incr(key, by, def, exp);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by, def, exp);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by, def, exp);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by, def, exp);
    }
  }

  /**
   * Increment the given counter, returning the new value.
   * <p/>
   * Due to the way the memcached server operates on items, incremented and
   * decremented items will be returned as Strings with any operations that
   * return a value.
   *
   * @param key the key
   * @param by  the amount to increment
   * @param def the default value (if the counter does not exist)
   * @param exp the expiration of this object
   * @return the new value, or -1 if we were unable to increment or add
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public long incr(String key, int by, long def, int exp) {
    try {
      return getMemcachedClient().incr(key, by, def, exp);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by, def, exp);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by, def, exp);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by, def, exp);
    }
  }

  /**
   * Decrement the given counter, returning the new value.
   * <p/>
   * Due to the way the memcached server operates on items, incremented and
   * decremented items will be returned as Strings with any operations that
   * return a value.
   *
   * @param key the key
   * @param by  the amount to decrement
   * @param def the default value (if the counter does not exist)
   * @param exp the expiration of this object
   * @return the new value, or -1 if we were unable to decrement or add
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public long decr(String key, long by, long def, int exp) {
    try {
      return getMemcachedClient().decr(key, by, def, exp);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by, def, exp);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by, def, exp);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by, def, exp);
    }
  }

  /**
   * Decrement the given counter, returning the new value.
   * <p/>
   * Due to the way the memcached server operates on items, incremented and
   * decremented items will be returned as Strings with any operations that
   * return a value.
   *
   * @param key the key
   * @param by  the amount to decrement
   * @param def the default value (if the counter does not exist)
   * @param exp the expiration of this object
   * @return the new value, or -1 if we were unable to decrement or add
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public long decr(String key, int by, long def, int exp) {
    try {
      return getMemcachedClient().decr(key, by, def, exp);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by, def, exp);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by, def, exp);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by, def, exp);
    }
  }


  /**
   * Asychronous increment.
   *
   * @param key key to increment
   * @param by  the amount to increment the value by
   * @return a future with the incremented value, or -1 if the increment failed.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Long> asyncIncr(String key, long by) {
    try {
      return getMemcachedClient().asyncIncr(key, by);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by);
    }
  }

  /**
   * Asychronous increment.
   *
   * @param key key to increment
   * @param by  the amount to increment the value by
   * @return a future with the incremented value, or -1 if the increment failed.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Long> asyncIncr(String key, int by) {
    try {
      return getMemcachedClient().asyncIncr(key, by);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by);
    }
  }

  /**
   * Asynchronous decrement.
   *
   * @param key key to decrement
   * @param by  the amount to decrement the value by
   * @return a future with the decremented value, or -1 if the decrement failed.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Long> asyncDecr(String key, long by) {
    try {
      return getMemcachedClient().asyncDecr(key, by);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by);
    }
  }

  /**
   * Asynchronous decrement.
   *
   * @param key key to decrement
   * @param by  the amount to decrement the value by
   * @return a future with the decremented value, or -1 if the decrement failed.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Long> asyncDecr(String key, int by) {
    try {
      return getMemcachedClient().asyncDecr(key, by);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by);
    }
  }

  /**
   * Asychronous increment.
   *
   * @param key key to increment
   * @param by  the amount to increment the value by
   * @param def the default value (if the counter does not exist)
   * @param exp the expiration of this object
   * @return a future with the incremented value, or -1 if the increment failed.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Long> asyncIncr(String key, long by, long def, int exp) {
    try {
      return getMemcachedClient().asyncIncr(key, by, def, exp);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by, def, exp);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by, def, exp);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by, def, exp);
    }
  }

  /**
   * Asychronous increment.
   *
   * @param key key to increment
   * @param by  the amount to increment the value by
   * @param def the default value (if the counter does not exist)
   * @param exp the expiration of this object
   * @return a future with the incremented value, or -1 if the increment failed.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Long> asyncIncr(String key, int by, long def, int exp) {
    try {
      return getMemcachedClient().asyncIncr(key, by, def, exp);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by, def, exp);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by, def, exp);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by, def, exp);
    }
  }

  /**
   * Asynchronous decrement.
   *
   * @param key key to decrement
   * @param by  the amount to decrement the value by
   * @param def the default value (if the counter does not exist)
   * @param exp the expiration of this object
   * @return a future with the decremented value, or -1 if the decrement failed.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Long> asyncDecr(String key, long by, long def, int exp) {
    try {
      return getMemcachedClient().asyncDecr(key, by, def, exp);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by, def, exp);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by, def, exp);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by, def, exp);
    }
  }

  /**
   * Asynchronous decrement.
   *
   * @param key key to decrement
   * @param by  the amount to decrement the value by
   * @param def the default value (if the counter does not exist)
   * @param exp the expiration of this object
   * @return a future with the decremented value, or -1 if the decrement failed.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Long> asyncDecr(String key, int by, long def, int exp) {
    try {
      return getMemcachedClient().asyncDecr(key, by, def, exp);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by, def, exp);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by, def, exp);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by, def, exp);
    }
  }

  /**
   * Asychronous increment.
   *
   * @param key key to increment
   * @param by  the amount to increment the value by
   * @param def the default value (if the counter does not exist)
   * @return a future with the incremented value, or -1 if the increment failed.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Long> asyncIncr(String key, long by, long def) {
    try {
      return getMemcachedClient().asyncIncr(key, by, def);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by, def);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by, def);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by, def);
    }
  }

  /**
   * Asychronous increment.
   *
   * @param key key to increment
   * @param by  the amount to increment the value by
   * @param def the default value (if the counter does not exist)
   * @return a future with the incremented value, or -1 if the increment failed.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Long> asyncIncr(String key, int by, long def) {
    try {
      return getMemcachedClient().asyncIncr(key, by, def);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by, def);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by, def);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncIncr(key, by, def);
    }
  }

  /**
   * Asynchronous decrement.
   *
   * @param key key to decrement
   * @param by  the amount to decrement the value by
   * @param def the default value (if the counter does not exist)
   * @return a future with the decremented value, or -1 if the decrement failed.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Long> asyncDecr(String key, long by, long def) {
    try {
      return getMemcachedClient().asyncDecr(key, by, def);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by, def);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by, def);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by, def);
    }
  }

  /**
   * Asynchronous decrement.
   *
   * @param key key to decrement
   * @param by  the amount to decrement the value by
   * @param def the default value (if the counter does not exist)
   * @return a future with the decremented value, or -1 if the decrement failed.
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Long> asyncDecr(String key, int by, long def) {
    try {
      return getMemcachedClient().asyncDecr(key, by, def);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by, def);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by, def);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().asyncDecr(key, by, def);
    }
  }

  /**
   * Increment the given counter, returning the new value.
   *
   * @param key the key
   * @param by  the amount to increment
   * @param def the default value (if the counter does not exist)
   * @return the new value, or -1 if we were unable to increment or add
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public long incr(String key, long by, long def) {
    try {
      return getMemcachedClient().incr(key, by, def);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by, def);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by, def);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by, def);
    }
  }

  /**
   * Increment the given counter, returning the new value.
   *
   * @param key the key
   * @param by  the amount to increment
   * @param def the default value (if the counter does not exist)
   * @return the new value, or -1 if we were unable to increment or add
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public long incr(String key, int by, long def) {
    try {
      return getMemcachedClient().incr(key, by, def);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by, def);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by, def);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().incr(key, by, def);
    }
  }

  /**
   * Decrement the given counter, returning the new value.
   *
   * @param key the key
   * @param by  the amount to decrement
   * @param def the default value (if the counter does not exist)
   * @return the new value, or -1 if we were unable to decrement or add
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public long decr(String key, long by, long def) {
    try {
      return getMemcachedClient().decr(key, by, def);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by, def);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by, def);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by, def);
    }
  }

  /**
   * Decrement the given counter, returning the new value.
   *
   * @param key the key
   * @param by  the amount to decrement
   * @param def the default value (if the counter does not exist)
   * @return the new value, or -1 if we were unable to decrement or add
   * @throws net.spy.memcached.OperationTimeoutException if the global operation timeout is
   *                                                     exceeded
   * @throws IllegalStateException                       in the rare circumstance where queue is too
   *                                                     full to accept any more requests
   */
  @Override
  public long decr(String key, int by, long def) {
    try {
      return getMemcachedClient().decr(key, by, def);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by, def);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by, def);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().decr(key, by, def);
    }
  }

  /**
   * Delete the given key from the cache.
   * <p/>
   * <p>
   * The hold argument specifies the amount of time in seconds (or Unix time
   * until which) the client wishes the server to refuse "add" and "replace"
   * commands with this key. For this amount of item, the item is put into a
   * delete queue, which means that it won't possible to retrieve it by the
   * "get" command, but "add" and "replace" command with this key will also fail
   * (the "set" command will succeed, however). After the time passes, the item
   * is finally deleted from server memory.
   * </p>
   *
   * @param key  the key to delete
   * @param hold how long the key should be unavailable to add commands
   * @return whether or not the operation was performed
   * @deprecated Hold values are no longer honored.
   */
  @Deprecated
  public OperationFuture<Boolean> delete(String key, int hold) {
    return delete(key);
  }

  /**
   * Delete the given key from the cache.
   *
   * @param key the key to delete
   * @return whether or not the operation was performed
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Boolean> delete(String key) {
    try {
      return getMemcachedClient().delete(key);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().delete(key);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().delete(key);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().delete(key);
    }
  }

  /**
   * Delete the given key from the cache of the given CAS value applies.
   *
   * @param key the key to delete
   * @param cas the CAS value to apply.
   * @return whether or not the operation was performed
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Boolean> delete(String key, long cas) {
    try {
      return getMemcachedClient().delete(key, cas);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().delete(key, cas);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().delete(key, cas);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().delete(key, cas);
    }
  }

  /**
   * Flush all caches from all servers with a delay of application.
   *
   * @param delay the period of time to delay, in seconds
   * @return whether or not the operation was accepted
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Boolean> flush(final int delay) {
    try {
      return getMemcachedClient().flush(delay);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().flush(delay);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().flush(delay);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().flush(delay);
    }
  }

  /**
   * Flush all caches from all servers immediately.
   *
   * @return whether or not the operation was performed
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public OperationFuture<Boolean> flush() {
    try {
      return getMemcachedClient().flush();
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().flush();
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().flush();
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().flush();
    }
  }

  @Override
  public Set<String> listSaslMechanisms() {
    try {
      return getMemcachedClient().listSaslMechanisms();
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().listSaslMechanisms();
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().listSaslMechanisms();
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().listSaslMechanisms();
    }
  }

  /**
   * Shut down immediately.
   */
  @Override
  public void shutdown() {
    try {
      getMemcachedClient().shutdown();
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      getMemcachedClient().shutdown();
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      getMemcachedClient().shutdown();
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      getMemcachedClient().shutdown();
    }
  }

  /**
   * Shut down this client gracefully.
   *
   * @param timeout the amount of time time for shutdown
   * @param unit    the TimeUnit for the timeout
   * @return result of the shutdown request
   */
  @Override
  public boolean shutdown(long timeout, TimeUnit unit) {
    try {
      return getMemcachedClient().shutdown(timeout, unit);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().shutdown(timeout, unit);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().shutdown(timeout, unit);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().shutdown(timeout, unit);
    }
  }

  /**
   * Wait for the queues to die down.
   *
   * @param timeout the amount of time time for shutdown
   * @param unit    the TimeUnit for the timeout
   * @return result of the request for the wait
   * @throws IllegalStateException in the rare circumstance where queue is too
   *                               full to accept any more requests
   */
  @Override
  public boolean waitForQueues(long timeout, TimeUnit unit) {
    try {
      return getMemcachedClient().waitForQueues(timeout, unit);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().waitForQueues(timeout, unit);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().waitForQueues(timeout, unit);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().waitForQueues(timeout, unit);
    }
  }

  /**
   * Add a connection observer.
   * <p/>
   * If connections are already established, your observer will be called with
   * the address and -1.
   *
   * @param obs the ConnectionObserver you wish to add
   * @return true if the observer was added.
   */
  @Override
  public boolean addObserver(ConnectionObserver obs) {
    try {
      return getMemcachedClient().addObserver(obs);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().addObserver(obs);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().addObserver(obs);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().addObserver(obs);
    }
  }

  /**
   * Remove a connection observer.
   *
   * @param obs the ConnectionObserver you wish to add
   * @return true if the observer existed, but no longer does
   */
  @Override
  public boolean removeObserver(ConnectionObserver obs) {
    try {
      return getMemcachedClient().removeObserver(obs);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().removeObserver(obs);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().removeObserver(obs);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().removeObserver(obs);
    }
  }

  @Override
  public void connectionEstablished(SocketAddress sa, int reconnectCount) {
    try {
      getMemcachedClient().connectionEstablished(sa, reconnectCount);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      getMemcachedClient().connectionEstablished(sa, reconnectCount);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      getMemcachedClient().connectionEstablished(sa, reconnectCount);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      getMemcachedClient().connectionEstablished(sa, reconnectCount);
    }
  }


  @Override
  public void connectionLost(SocketAddress sa) {
    try {
      getMemcachedClient().connectionLost(sa);
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      getMemcachedClient().connectionLost(sa);
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      getMemcachedClient().connectionLost(sa);
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      getMemcachedClient().connectionLost(sa);
    }
  }

  @Override
  public String toString() {
    try {
      return getMemcachedClient().toString();
    } catch (IllegalStateException e) {
      log.warn(e);
      init();
      return getMemcachedClient().toString();
    } catch (OperationTimeoutException e) {
      log.warn(e);
      init();
      return getMemcachedClient().toString();
    } catch (RuntimeException e) {
      log.warn(e);
      init();
      return getMemcachedClient().toString();
    }
  }

}
