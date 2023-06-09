package com.ca.commons.cbutil;

import java.awt.*;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.zip.ZipException;

/**
 * This Class provides access to a group of zip files.  In the case of namespace conflicts between
 * Zip files, the first zip file registered has priority.  The zip files are accessed through CBJarResource.
 *
 * @author Chris Betts
 * @see com.ca.commons.cbutil.CBJarResource
 */

public class CBResourceLoader {

    /**
     * a cached list of 'unknown' resources that have been (unsuccessfully) looked up before.
     */

    protected HashSet unknownResources = new HashSet();

    /**
     * A list of CBJarResources objects that may be searched for packaged resources.
     */

    protected CBJarResource[] resourceFiles = null;

    /**
     * Whether to print out shtuff on the way.
     */

    protected static boolean debug = false;

    private static Logger log = Logger.getLogger(CBResourceLoader.class.getName());

    /**
     * Constructor - note that that the class is useless until at least one resource file has been
     * registered with it using the addResource() method.
     */

    public CBResourceLoader() {

        log.fine("Started CBResourceLoader");
    }

    /**
     * Adds a new zip/jar resource file to the list of files to be examined for resources.
     */

    public void addResource(CBJarResource resource) {

        int size = (resourceFiles == null) ? 0 : (resourceFiles.length);

        // The clumsiness of using an array is balanced by the need for quick
        // access, and the fact that normally only a small number of resources are added,
        // and only at the start of program operation.

        CBJarResource[] newArray = new CBJarResource[size + 1];
        for (int i = 0; i < size; i++)
            newArray[i] = resourceFiles[i];
        newArray[size] = resource;
        resourceFiles = newArray;

        log.fine("Added CBJarResource: " + resource.toString());
    }

    /**
     * Search all registered zip files for a particular file, and return an input stream to that
     * file.
     */

    public InputStream getInputStream(String resourceName) throws ZipException {

        CBJarResource resourceFile = getJarContainingResource(resourceName);
        if (resourceFile != null)
            return resourceFile.getInputStream(resourceName);

        throw new ZipException("File: '" + resourceName + "' not found");
    }

    /**
     * Search all registered zip files for a particular Image, and return an awt Image
     * object.
     */

    public Image getImage(String imageName, Toolkit imageCreator) throws ZipException {

        CBJarResource resourceFile = getJarContainingResource(imageName);
        if (resourceFile != null)
            return resourceFile.getImage(imageName, imageCreator);

        throw new ZipException("Image File: '" + imageName + "' not found");
    }

    /**
     * Search all registered zip files for a particular file, and return the data within that
     * file as a byte array.  (This might be used to initialise a String object if the file is
     * a text file, for instance.)
     */

    public byte[] getResource(String resourceName) throws ZipException {

        log.finer("  $$ getting resource '" + resourceName + "' from CBResourceLoader");
        CBJarResource resourceFile = getJarContainingResource(resourceName);
        if (resourceFile != null) {
            log.finer("  $$ found resource '" + resourceName + "' in zip file '" + resourceFile.getZipFileName() + "' - extracting");
            return resourceFile.getResource(resourceName);
        }
        throw new ZipException("File: '" + resourceName + "' not found");
    }

    public long getLastModified(String resourceName) throws ZipException {

        CBJarResource resourceFile = getJarContainingResource(resourceName);
        if (resourceFile != null) {
            return resourceFile.getLastModified();
        }
        throw new ZipException("File: '" + resourceName + "' not found");
    }

    /**
     * Searches an internal hash, followed by all Jar files, for a particular resource.
     */
    public CBJarResource getJarContainingResource(String resourceName) {

        if (resourceFiles == null) return null;  // we don't have any resources...

        // check to see if we've already looked for this resource.
        if (unknownResources.contains(resourceName)) return null;

        for (int i = 0; i < resourceFiles.length; i++)
            if (resourceFiles[i].hasResource(resourceName))
                return resourceFiles[i];

        // nothing found!  Add an entry to the unknownResources hashset so that we don't look for
        // it again.

        unknownResources.add(resourceName);

        return null; // nothing found
    }

    /**
     * Returns all resources with the given prefix.
     *
     * @param prefix a string to match the start of resources against, e.g. 'icons/'
     */

    public String[] getPrefixedResources(String prefix) {

        if (resourceFiles == null) return new String[]{};  // don't have anything.

        Vector resources = new Vector();

        // cycle through all resource files, gathering prefixed resources.
        // name clashes are simply included twice :-)

        for (int i = 0; i < resourceFiles.length; i++) {
            resources.addAll(Arrays.asList(resourceFiles[i].getPrefixedResources(prefix)));
        }

        // cast stuff back to string for return.
        if (resources.size() == 0) {
            return new String[]{};
        } else {
            return (String[]) resources.toArray(new String[resources.size()]);
        }
    }

    /**
     * This is a very PRIMATIVE wildcard matching routine - it allows
     * only ONE wildcard, and that wildcard MUST be a '*' character.
     * (So in effect this is simply a prefix + suffix match).
     *
     * @param exp a SIMPLE wildcard expression to match, e.g. 'templates/plain*.html'
     */

/*
XXX Under Development...

    public String[] getWildCardResources(String exp)
    {
        if (resourceFiles == null) return new String[]{};  // don't have anything.to search

        int wildpos = exp.indexOf('*');
        if (wildpos == -1) return new String[]{exp};

        if (wildpos == exp.length() - 1) // i.e. last character
            return getPrefixedResources(exp.substring(0, exp.length() - 1));

        Vector resources = new Vector();

        // cycle through all resource files, gathering prefixed resources.
        // name clashes are simply included twice :-)

        String prefix = exp.substring(0, wildpos);
        String suffix = exp.substring(wildpos + 1);

        for (int i = 0; i < resourceFiles.length; i++)
        {
            resources.addAll(Arrays.asList(resourceFiles[i].getBoundedResources(prefix, suffix)));
        }

        // cast stuff back to string for return.
        if (resources.size() == 0)
        {
            return new String[]{};
        }
        else
        {
            return (String[]) resources.toArray(new String[resources.size()]);
        }
    }
*/
}