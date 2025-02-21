package com.nvk.cinemav.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class CloudinaryService {
  @Autowired
  private Cloudinary cloudinary;

//  public String uploadImage(MultipartFile file) {
//    try {
//      Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//      return uploadResult.get("url").toString();
//    } catch (IOException e) {
//      throw new RuntimeException("Failed to upload image", e);
//    }
//  }

  // Upload hình ảnh
  public String uploadImage(MultipartFile file) {
    return uploadFile(file, "image");
  }

  // Upload video
  public String uploadVideo(MultipartFile file) {
    return uploadFile(file, "video");
  }

  // Hàm chung để upload file
  private String uploadFile(MultipartFile file, String resourceType) {
    try {
      Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
          ObjectUtils.asMap("resource_type", resourceType));
      return uploadResult.get("url").toString();
    } catch (IOException e) {
      throw new RuntimeException("Failed to upload " + resourceType, e);
    }
  }


  // Xóa file theo URL
  public String deleteFileByUrl(String fileUrl, String resourceType) {
    String publicId = extractPublicId(fileUrl);
    if (publicId == null) {
      throw new RuntimeException("Invalid Cloudinary URL");
    }

    try {
      Map result = cloudinary.uploader().destroy(publicId,
          ObjectUtils.asMap("resource_type", resourceType));
      return result.get("result").toString();
    } catch (Exception e) {
      throw new RuntimeException("Failed to delete file", e);
    }
  }

  // Trích xuất public_id từ URL
  private String extractPublicId(String fileUrl) {
    String regex = ".*/upload/v\\d+/(.*)\\..*";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(fileUrl);

    if (matcher.matches()) {
      return matcher.group(1);
    }
    return null;
  }
}
