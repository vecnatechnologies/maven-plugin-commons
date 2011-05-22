/**
 * Copyright 2011 Vecna Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
*/

package com.vecna.maven.commons;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Base class for invoking Mojos with a custom classloader. 
 * 
 * @author ogolberg@vecna.com
 */
public abstract class AbstractClassLoaderSwappingMojo extends AbstractMojo {
  /**
   * Get the custom class loader
   * @return the custom class loader to use
   * @throws MojoExecutionException if an error occurs
   * @throws MojoFailureException if an error occurs
   */
  protected abstract ClassLoader getClassLoader() throws MojoExecutionException, MojoFailureException;  

  /**
   * This method will be executed in the custom class loader context
   * @throws MojoExecutionException if an error occurs
   * @throws MojoFailureException if an error occurs
   */
  protected abstract void executeWithClassLoader() throws MojoExecutionException, MojoFailureException;
  
  /**
   * {@inheritDoc}
   */
  public void execute() throws MojoExecutionException, MojoFailureException {
    final Thread currentThread = Thread.currentThread();    
    final ClassLoader oldClassLoader = currentThread.getContextClassLoader();
    try {
      currentThread.setContextClassLoader(getClassLoader());
      executeWithClassLoader();
    } finally {
      currentThread.setContextClassLoader(oldClassLoader);
    }
  }
}
