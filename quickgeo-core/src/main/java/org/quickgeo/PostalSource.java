/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package org.quickgeo;

import java.io.InputStream;

/**
 * The PostalSource interface is used by modules which have postal data to
 * be included in QuickGeo.  More than likely you don't need to interact 
 * with this Interface directly.
 * 
 * @author Jason Nichols (jason@kickroot.com)
 * @since 0.1.0
 */
public interface PostalSource {
  
  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  public InputStream getStream();
}
