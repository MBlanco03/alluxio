/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.cli;

import alluxio.cli.fs.FileSystemShell;
import alluxio.cli.fs.FileSystemShellUtils;
import alluxio.cli.fsadmin.FileSystemAdminShell;
import alluxio.cli.fsadmin.FileSystemAdminShellUtils;
import alluxio.client.file.FileSystemContext;
import alluxio.conf.InstancedConfiguration;
import alluxio.conf.PropertyKey;
import alluxio.conf.ServerConfiguration;
import alluxio.util.ConfigurationUtils;
import alluxio.util.io.PathUtils;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.Closer;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.zookeeper.server.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.annotation.concurrent.ThreadSafe;

/**
 * CSV_FILE_DIR utility to generate property keys to csv files.
 */
@ThreadSafe
public final class ConfigurationDocGenerator {
  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationDocGenerator.class);
  private static final String CSV_FILE_DIR = "docs/_data/table/";
  private static final String YML_FILE_DIR = "docs/_data/table/en/";
  public static final String CSV_FILE_HEADER = "propertyName,defaultValue";

  //CLI Variables
  private static final String FS_YML_DIR = "docs/_data/table/en/cli/fs";
  private static final String FSADMIN_YML_DIR = "docs/_data/table/en/cli/fsadmin";

  private ConfigurationDocGenerator() {} // prevent instantiation

  /**
   * Writes property key to csv files.
   *
   * @param defaultKeys Collection which is from PropertyKey DEFAULT_KEYS_MAP.values()
   * @param filePath    path for csv files
   */
  @VisibleForTesting
  public static void writeCSVFile(Collection<? extends PropertyKey> defaultKeys, String filePath)
      throws IOException {
    if (defaultKeys.size() == 0) {
      return;
    }

    FileWriter fileWriter;
    Closer closer = Closer.create();
    String[] fileNames = {"user-configuration.csv", "master-configuration.csv",
        "worker-configuration.csv", "security-configuration.csv",
        "common-configuration.csv", "cluster-management-configuration.csv"};

    try {
      // HashMap for FileWriter per each category
      Map<String, FileWriter> fileWriterMap = new HashMap<>();
      for (String fileName : fileNames) {
        fileWriter = new FileWriter(PathUtils.concatPath(filePath, fileName));
        // Write the CSV file header and line separator after the header
        fileWriter.append(CSV_FILE_HEADER + "\n");
        //put fileWriter
        String key = fileName.substring(0, fileName.indexOf("configuration") - 1);
        fileWriterMap.put(key, fileWriter);
        //register file writer
        closer.register(fileWriter);
      }

      // Sort defaultKeys
      List<PropertyKey> dfkeys = new ArrayList<>(defaultKeys);
      Collections.sort(dfkeys);

      for (PropertyKey propertyKey : dfkeys) {
        String pKey = propertyKey.toString();
        String defaultDescription;
        if (propertyKey.getDefaultSupplier().get() == null) {
          defaultDescription = "";
        } else {
          defaultDescription = propertyKey.getDefaultSupplier().getDescription();
        }
        // Quote the whole description to escape characters such as commas.
        defaultDescription = String.format("\"%s\"", defaultDescription);

        // Write property key and default value to CSV
        String keyValueStr = pKey + "," + defaultDescription + "\n";
        if (pKey.startsWith("alluxio.user.")) {
          fileWriter = fileWriterMap.get("user");
        } else if (pKey.startsWith("alluxio.master.")) {
          fileWriter = fileWriterMap.get("master");
        } else if (pKey.startsWith("alluxio.worker.")) {
          fileWriter = fileWriterMap.get("worker");
        } else if (pKey.startsWith("alluxio.security.")) {
          fileWriter = fileWriterMap.get("security");
        } else if (pKey.startsWith("alluxio.integration")) {
          fileWriter = fileWriterMap.get("cluster-management");
        } else {
          fileWriter = fileWriterMap.get("common");
        }
        fileWriter.append(keyValueStr);
      }

      LOG.info("Property Key CSV files were created successfully.");
    } catch (Exception e) {
      throw closer.rethrow(e);
    } finally {
      try {
        closer.close();
      } catch (IOException e) {
        LOG.error("Error while flushing/closing Property Key CSV FileWriter", e);
      }
    }
  }

  /**
   * Writes description of property key to yml files.
   *
   * @param defaultKeys Collection which is from PropertyKey DEFAULT_KEYS_MAP.values()
   * @param filePath path for csv files
   */
  @VisibleForTesting
  public static void writeYMLFile(Collection<? extends PropertyKey> defaultKeys, String filePath)
      throws IOException {
    if (defaultKeys.size() == 0) {
      return;
    }

    FileWriter fileWriter;
    Closer closer = Closer.create();
    String[] fileNames = {"user-configuration.yml", "master-configuration.yml",
        "worker-configuration.yml", "security-configuration.yml",
        "common-configuration.yml", "cluster-management-configuration.yml"
    };

    try {
      // HashMap for FileWriter per each category
      Map<String, FileWriter> fileWriterMap = new HashMap<>();
      for (String fileName : fileNames) {
        fileWriter = new FileWriter(PathUtils.concatPath(filePath, fileName));
        //put fileWriter
        String key = fileName.substring(0, fileName.indexOf("configuration") - 1);
        fileWriterMap.put(key, fileWriter);
        //register file writer
        closer.register(fileWriter);
      }

      // Sort defaultKeys
      List<PropertyKey> dfkeys = new ArrayList<>(defaultKeys);
      Collections.sort(dfkeys);

      for (PropertyKey iteratorPK : dfkeys) {
        String pKey = iteratorPK.toString();

        // Puts descriptions in single quotes to avoid having to escaping reserved characters.
        // Still needs to escape single quotes with double single quotes.
        String description = iteratorPK.getDescription().replace("'", "''");

        // Write property key and default value to yml files
        if (iteratorPK.isIgnoredSiteProperty()) {
          description += " Note: overwriting this property will only work when it is passed as a "
              + "JVM system property (e.g., appending \"-D" + iteratorPK + "\"=<NEW_VALUE>\" to "
              + "$ALLUXIO_JAVA_OPTS). Setting it in alluxio-site.properties will not work.";
        }
        String keyValueStr = pKey + ":\n  '" + description + "'\n";
        if (pKey.startsWith("alluxio.user.")) {
          fileWriter = fileWriterMap.get("user");
        } else if (pKey.startsWith("alluxio.master.")) {
          fileWriter = fileWriterMap.get("master");
        } else if (pKey.startsWith("alluxio.worker.")) {
          fileWriter = fileWriterMap.get("worker");
        } else if (pKey.startsWith("alluxio.security.")) {
          fileWriter = fileWriterMap.get("security");
        } else if (pKey.startsWith("alluxio.integration.")) {
          fileWriter = fileWriterMap.get("cluster-management");
        } else {
          fileWriter = fileWriterMap.get("common");
        }
        fileWriter.append(StringEscapeUtils.escapeHtml(keyValueStr));
      }

      LOG.info("YML files for description of Property Keys were created successfully.");
    } catch (Exception e) {
      throw closer.rethrow(e);
    } finally {
      try {
        closer.close();
      } catch (IOException e) {
        LOG.error("Error while flushing/closing YML files for description of Property Keys "
            + "FileWriter", e);
      }
    }
  }

  public static void writeCommandDocs(Map<String, Command> commands, String filePath)
      throws IOException {
    if (commands.size() == 0) {
      return;
    }
    FileWriter fileWriter;
    Closer closer = Closer.create();

    try {
      for (Map.Entry<String, Command> mt : commands.entrySet()) {
        fileWriter = new FileWriter(PathUtils.concatPath(filePath, mt.getValue().getCommandName() + ".yml"));
        closer.register(fileWriter);
        fileWriter.append(mt.getValue().getDocumentation());
        if (!mt.getValue().getOptions().getOptions().isEmpty()) {
          fileWriter.append("options: |\n");
        }
        for (Option commandOpt:mt.getValue().getOptions().getOptions()){
          if (commandOpt.getOpt() == null){
            fileWriter.append("  - `--"+ commandOpt.getLongOpt()+ "` ");
          }
            else{ fileWriter.append("  - `-"+ commandOpt.getOpt()+ "` "); }
          fileWriter.append(commandOpt.getDescription() + "\n");
        }
        fileWriter.append("example: |\n  ");
        fileWriter.append(mt.getValue().getExample());
      }

      LOG.info("Command YML files were created successfully.");
    } catch(Exception e){
        throw closer.rethrow(e);
      } finally{
        try {
          closer.close();
        } catch (IOException e) {
          LOG.error("Error while flushing/closing YML files for documentation of commands "
                  + "FileWriter", e);
        }
      }
    }

  /**
   * Main entry for this util class.
   *
   * @param args arguments for command line
   */
  public static void main(String[] args) throws IOException {
    Collection<? extends PropertyKey> defaultKeys = PropertyKey.defaultKeys();
    defaultKeys.removeIf(key -> key.isHidden());
    InstancedConfiguration c = new InstancedConfiguration(ConfigurationUtils.defaults());
    c.set(PropertyKey.HOME, System.getProperty("user.dir"));
    String homeDir = c.get(PropertyKey.HOME);

    // generate CSV files
    String filePath = PathUtils.concatPath(homeDir, CSV_FILE_DIR);
    writeCSVFile(defaultKeys, filePath);
    // generate YML files
    filePath = PathUtils.concatPath(homeDir, YML_FILE_DIR);
    writeYMLFile(defaultKeys, filePath);

    //For Alluxio Commands
    Closer mCloser = Closer.create();
    Map<String, Command> fsCommands = FileSystemShellUtils.loadCommands(mCloser.register(FileSystemContext.create(ServerConfiguration.global())));
    filePath = PathUtils.concatPath(homeDir, FS_YML_DIR);
    writeCommandDocs(fsCommands, filePath);

    Map<String, Command> fsadminCommands = new FileSystemAdminShell(ServerConfiguration.global()).loadCommands();
    filePath = PathUtils.concatPath(homeDir,FSADMIN_YML_DIR);
    writeCommandDocs(fsadminCommands,filePath);

  }
}
