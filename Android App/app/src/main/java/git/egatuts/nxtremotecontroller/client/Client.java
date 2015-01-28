package git.egatuts.nxtremotecontroller.client;

import git.egatuts.nxtremotecontroller.GlobalUtils;

public class Client {

  public String name;
  public String email;
  public double latitude;
  public double longitude;
  public String shortLocation;
  public String longLocation;
  public String profileImageURL;

  public void setName (String name) {
    this.name = name;
  }

  public String getName () {
    return this.name;
  }

  public void setEmail (String email) {
    this.email = email;
    this.profileImageURL = "https://www.gravatar.com/avatar/" + GlobalUtils.md5(email) + ".png?s=150&d=blank";
  }

  public String getEmail () {
    return this.email;
  }

  public void setLongitude (double longitude) {
    this.longitude = longitude;
  }

  public double getLongitude () {
    return this.longitude;
  }

  public void setLatitude (double latitude) {
    this.latitude = latitude;
  }

  public double getLatitude () {
    return this.latitude;
  }

  public void setShortLocation (String location) {
    this.shortLocation = location;
  }

  public String getShortLocation () {
    return this.shortLocation;
  }

  public void setLongLocation (String location) {
    this.longLocation = location;
  }

  public String getLongLocation () {
    return this.longLocation;
  }

  public String getProfileImageURL () {
    return this.profileImageURL;
  }

}
