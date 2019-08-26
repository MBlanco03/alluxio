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

/**
 * command object for reading yaml files.
 */
public class CommandReader {
  private String mName = null;
  private  String mUsage = null;
  private String mDescription = null;
  private String mExample = null;
  private  String mSubCommands = null;
  private String[] mOptions = null;

  /**
   *
   * @param name of command
   */
  public void setName(String name) {
    mName = name;
  }

  /**
   *
   * @return command name
   */
  public String getName() {
    return this.mName;
  }

  /**
   *
   * @param Usage of command
   */
  public void setUsage(String Usage) {
    mUsage = Usage;
  }

  /**
   *
   * @return command usage
   */
  public String getUsage() {
    return this.mUsage;
  }

  /**
   *
   * @param Description of command
   */
  public void setDescription(String Description) {
    mDescription = Description;
  }

  /**
   *
   * @return command description
   */
  public String getDescription() {
    return this.mDescription;
  }

  /**
   *
   * @param Example of command
   */
  public void setExample(String Example) {
    mExample = Example;
  }

  /**
   *
   * @return example command
   */
  public  String getExample() {
    return this.mExample;
  }

  /**
   *
   * @param subCommands of command
   */
  public void setSubCommands(String subCommands) {
    mSubCommands = subCommands;
  }

  /**
   *
   * @return command subcommands
   */
  public String getSubCommands() {
    return this.mSubCommands;
  }

  /**
   *
   * @param options of command
   */
  public void setOptions(String[] options) {
    mOptions = options;
  }

  /**
  *
  * @return command options
  */
  public String[] getOptions() {
    return this.mOptions;
  }

  @Override
  public String toString() {
    return "name: " + getName()
            + "\nusage: " + getUsage()
            + "\ndescription: |\n  " + getDescription();
  }
}
