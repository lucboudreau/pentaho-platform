/*!
 *
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 *
 * Copyright (c) 2018-2018 Hitachi Vantara. All rights reserved.
 *
 */

package org.pentaho.platform.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

import org.pentaho.platform.api.cache.IPlatformCache;
import org.pentaho.platform.api.engine.IPentahoSession;

/**
 * A mocked platform cache which uses a hash map as a backing store.
 */
public class MockPlatformCache implements IPlatformCache {

  Map<CacheScope, Map> inMemoryCache = new ConcurrentHashMap<>();
  public void onLogout( IPentahoSession session ) {
    inMemoryCache.remove( CacheScope.forSession( session ) );
  }
  static public class MockCache implements IPlatformCache.Cache {
    private CacheScope scope;
    Map<CacheScope, Map> inMemoryCache;
    public MockCache( CacheScope scope, Map inMemoryCache ) {
      this.scope = scope;
      this.inMemoryCache = inMemoryCache;
    }
    public void put( Object key, Object value ) {
      if ( !inMemoryCache.containsKey( scope ) ) {
        inMemoryCache.put( scope, new HashMap<>() );
      }
      inMemoryCache.get( scope ).put( key, value );
    }
    public Set<Entry> entrySet() {
      if ( !inMemoryCache.containsKey( scope ) ) {
        return Collections.emptySet();
      }
      return inMemoryCache.get( scope ).entrySet();
    }
    public Set keySet() {
      if ( !inMemoryCache.containsKey( scope ) ) {
        return Collections.emptySet();
      }
      return inMemoryCache.get( scope ).keySet();
    }
    public Set values() {
      if ( !inMemoryCache.containsKey( scope ) ) {
        return Collections.emptySet();
      }
      return new HashSet<>( inMemoryCache.get( scope ).values() );
    }
    public int size() {
      if ( !inMemoryCache.containsKey( scope ) ) {
        return 0;
      }
      return inMemoryCache.get( scope ).size();
    }
    public Object get( Object key ) {
      if ( !inMemoryCache.containsKey( scope ) ) {
        return null;
      }
      return inMemoryCache.get( scope ).get( key );
    }
    public void remove( Object key ) {
      if ( !inMemoryCache.containsKey( scope ) ) {
        return;
      }
      inMemoryCache.get( scope ).remove( key );
    }
    public void clear() {
      if ( !inMemoryCache.containsKey( scope ) ) {
        return;
      }
      inMemoryCache.get( scope ).clear();
    }
    public void clear( boolean delete ) {
      clear();
      if ( delete ) {
        inMemoryCache.remove( scope );
      }
    }
  }
  public void clearAll() {
    inMemoryCache.clear();
  }
  public void stop() {
    inMemoryCache.clear();
  }
  public void start() {
    // no op.
  }
  public boolean isEnabled() {
    return true;
  }
  public <K, V> Cache<K, V> getCache( CacheScope scope, Class<K> keyClass, Class<V> valueClass ) {
    return new MockCache( scope, inMemoryCache );
  }
  public Cache getCache( CacheScope scope ) {
    return new MockCache( scope, inMemoryCache );
  }
}
