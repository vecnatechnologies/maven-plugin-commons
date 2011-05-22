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

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Base class for invoking Mojos with project's build classpath settings.
 * 
 * @author ogolberg@vecna.com
 */
public abstract class BuildClassPathMojo extends AbstractClassLoaderSwappingMojo {
  /**
   * @return reference to the Maven project, usually Plexus-injected in the concrete subclass
   */
  protected abstract MavenProject getProject();

  /**
   * {@inheritDoc}
   */
  @Override
  protected ClassLoader getClassLoader() throws MojoExecutionException, MojoFailureException {    
    try {
      @SuppressWarnings("unchecked") List<String> classpathElements = getProject().getCompileClasspathElements();
      if (getLog().isDebugEnabled()) {
        getLog().debug("compile classpath: " + classpathElements);
      }
      classpathElements.add(getProject().getBuild().getOutputDirectory());
      List<URL> urls = new ArrayList<URL>(classpathElements.size());
      for (String classpathElement : classpathElements) {
        urls.add(new File(classpathElement).toURL());
      }
      if (getLog().isDebugEnabled()) {
        getLog().debug("setting classpath to " + urls);
      }
      return new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
    } catch (Exception e) {
      throw new MojoExecutionException("couldn't create the build classpath loader", e);
    }    
  }
}
