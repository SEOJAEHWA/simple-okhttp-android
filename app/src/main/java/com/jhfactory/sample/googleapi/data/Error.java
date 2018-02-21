package com.jhfactory.sample.googleapi.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Error implements Parcelable {

  public static final String PERMISSION_DENIED = "PERMISSION_DENIED";

  private DefaultError error;

  private Error(Parcel in) {
    error = in.readParcelable(DefaultError.class.getClassLoader());
  }

  public static final Creator<Error> CREATOR =
      new Creator<Error>() {
        @Override
        public Error createFromParcel(Parcel in) {
          return new Error(in);
        }

        @Override
        public Error[] newArray(int size) {
          return new Error[size];
        }
      };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeParcelable(error, i);
  }

  public DefaultError getError() {
    return error;
  }

  public static class DefaultError implements Parcelable {
    private int code;
    private String message;
    private String status;

    private DefaultError(Parcel in) {
      code = in.readInt();
      message = in.readString();
      status = in.readString();
    }

    public static final Creator<DefaultError> CREATOR =
        new Creator<DefaultError>() {
          @Override
          public DefaultError createFromParcel(Parcel in) {
            return new DefaultError(in);
          }

          @Override
          public DefaultError[] newArray(int size) {
            return new DefaultError[size];
          }
        };

    public int getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }

    public String getStatus() {
      return status;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
      parcel.writeInt(code);
      parcel.writeString(message);
      parcel.writeString(status);
    }
  }
}
