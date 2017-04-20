package info.krushik.retrofit2db.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Flower implements Parcelable {

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("instructions")
    @Expose
    private String instructions;

    @SerializedName("photo")
    @Expose
    private String photo;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("productId")
    @Expose
    private int productId;

    private Bitmap picture;
    private boolean isFromDatabase;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public boolean isFromDatabase() {
        return isFromDatabase;
    }

    public void setFromDatabase(boolean fromDatabase) {
        isFromDatabase = fromDatabase;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
        dest.writeDouble(this.price);
        dest.writeString(this.instructions);
        dest.writeString(this.photo);
        dest.writeString(this.name);
        dest.writeInt(this.productId);
        dest.writeParcelable(this.picture, flags);
        dest.writeByte(this.isFromDatabase ? (byte) 1 : (byte) 0);

    }

    public Flower() {
    }

    protected Flower(Parcel in) {
        this.category = in.readString();
        this.price = in.readDouble();
        this.instructions = in.readString();
        this.photo = in.readString();
        this.name = in.readString();
        this.productId = in.readInt();
        this.picture = in.readParcelable(Bitmap.class.getClassLoader());
        this.isFromDatabase = in.readByte() != 0;
    }

    public static final Creator<Flower> CREATOR = new Creator<Flower>() {
        @Override
        public Flower createFromParcel(Parcel source) {
            return new Flower(source);
        }

        @Override
        public Flower[] newArray(int size) {
            return new Flower[size];
        }
    };
}
//public class Flower implements Serializable {
//
//    private static final long serialVersionUID = 111696345129311948L;
//    public byte[] imageByteArray;
//
//    @Expose
//    private String category;
//
//    @Expose
//    private double price;
//
//    @Expose
//    private String instructions;
//
//    @Expose
//    private String photo;
//
//    @Expose
//    private String name;
//
//    @Expose
//    private int productId;
//
//    private Bitmap picture;
//
//    private boolean isFromDatabase;
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public String getInstructions() {
//        return instructions;
//    }
//
//    public void setInstructions(String instructions) {
//        this.instructions = instructions;
//    }
//
//    public String getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(String photo) {
//        this.photo = photo;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getProductId() {
//        return productId;
//    }
//
//    public void setProductId(int productId) {
//        this.productId = productId;
//    }
//
//    public void setPicture(Bitmap picture) {
//        this.picture = picture;
//    }
//
//    public Bitmap getPicture() {
//        return picture;
//    }
//
//    public boolean isFromDatabase() {
//        return isFromDatabase;
//    }
//
//    public void setFromDatabase(boolean fromDatabase) {
//        isFromDatabase = fromDatabase;
//    }
//
//
//    private void writeObject(ObjectOutputStream out) throws IOException {
//
//        out.writeObject(category);
//        out.writeObject(price);
//        out.writeObject(instructions);
//        out.writeObject(photo);
//        out.writeObject(name);
//        out.writeObject(productId);
//
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        picture.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
//        byte bitmapBytes[] = byteStream.toByteArray();
//        out.write(bitmapBytes, 0, bitmapBytes.length);
//    }
//
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//
//        category = (String) in.readObject();
//        price = (Double) in.readObject();
//        instructions = (String) in.readObject();
//        photo = (String) in.readObject();
//        name = (String) in.readObject();
//        productId = (Integer) in.readObject();
//
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        int b;
//        while ((b = in.read()) != -1)
//            byteStream.write(b);
//        byte bitmapBytes[] = byteStream.toByteArray();
//        picture = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
//    }
//}
