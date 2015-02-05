/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (c) 2014 EgaTuts & Esaú García - All Rights Reserved                *
 *                                                                               *
 * Open-source code licensed under the MIT License (the "License").              *
 *                                                                               *
 * Permission is hereby granted, free of charge, to any person obtaining a copy  *
 * of this software and associated documentation files (the "Software"), to deal *
 * in the Software without restriction, including without limitation the rights  *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell     *
 * copies of the Software, and to permit persons to whom the Software is         *
 * furnished to do so, subject to the following conditions:                      *
 *                                                                               *
 * The above copyright notice and this permission notice shall be included in    *
 * all copies or substantial portions of the Software.                           *
 *                                                                               *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR    *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,      *
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE   *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER        *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN     *
 * THE SOFTWARE.                                                                 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                               *
 *                                                                                                                                                   *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                                *
 *                                                                                                                                                   *
 * And the corresponding file at:                                                                                                                    *
 *                                                                                                                                                   *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/client/Client.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.client;

import org.json.JSONException;
import org.json.JSONObject;

import git.egatuts.nxtremotecontroller.utils.GlobalUtils;

/*
 * Represents a remote client that is able to control us (the robot).
 */
public class Client {

  public String id;
  public String socketId;
  public String peerId;
  public String name;
  public String email;
  public double latitude;
  public double longitude;
  public String shortLocation;
  public String longLocation;
  public String profileImageURL;

  /*
   *  Constructors.
   */
  public Client () {}

  public Client (String id, String socketId, String peerId, String name, String email, double latitude, double longitude, String shortLocation, String longLocation) {
    this.id = id;
    this.socketId = socketId;
    this.peerId = peerId;
    this.name = name;
    this.email = email;
    this.latitude = latitude;
    this.longitude = longitude;
    this.shortLocation = shortLocation;
    this.longLocation = longLocation;
  }

  /*
   *  Getter and setter for the first socket id.
   */
  public void setId (String id) {
    this.id = id;
  }

  public String getId () {
    return this.id;
  }

  /*
   *  Getter and setter for the socket id.
   */
  public void setSocketId (String socketId) {
    this.socketId = socketId;
  }

  public String getSocketId () {
    return this.socketId;
  }

  /*
   *  Getter and setter for the peer id.
   */
  public void setPeerId (String peerId) {
    this.peerId = peerId;
  }

  public String getPeerId () {
    return this.peerId;
  }

  /*
   *  Getter and setter for the name.
   */
  public void setName (String name) {
    this.name = name;
  }

  public String getName () {
    return this.name;
  }

  /*
   *  Getter and setter for the email.
   */
  public void setEmail (String email) {
    this.email = email;
    this.profileImageURL = "https://www.gravatar.com/avatar/" + GlobalUtils.md5(email) + ".png?s=150&d=blank";
  }

  public String getEmail () {
    return this.email;
  }

  /*
   *  Getter and setter for the longitude.
   */
  public void setLongitude (double longitude) {
    this.longitude = longitude;
  }

  public double getLongitude () {
    return this.longitude;
  }

  /*
   *  Getter and setter for the latitude.
   */
  public void setLatitude (double latitude) {
    this.latitude = latitude;
  }

  public double getLatitude () {
    return this.latitude;
  }

  /*
   *  Getter and setter for the sort location.
   */
  public void setShortLocation (String location) {
    this.shortLocation = location;
  }

  public String getShortLocation () {
    return this.shortLocation;
  }

  /*
   *  Getter and setter for the long location (complete) name.
   */
  public void setLongLocation (String location) {
    this.longLocation = location;
  }

  public String getLongLocation () {
    return this.longLocation;
  }

  /*
   *  Returns the profile image URL which uses gravatar service.
   */
  public String getProfileImageURL () {
    return this.profileImageURL;
  }

  /*
   *  Returns new Client instance by passing a JSON object with all its properties.
   */
  public static Client fromJSON (JSONObject member) throws JSONException {
    Client client = new Client();
    client.setName(member.getString("name"));
    client.setEmail(member.getString("email"));
    client.setShortLocation(member.getString("short_location"));
    client.setLongLocation(member.getString("long_location"));
    client.setLatitude(member.getDouble("latitude"));
    client.setLongitude(member.getDouble("longitude"));
    client.setSocketId(member.getString("id"));
    client.setPeerId(member.getString("peer"));
    return client;
  }

}
