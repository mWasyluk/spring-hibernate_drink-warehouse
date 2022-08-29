package com.example.hibernatetest.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "images")
@Slf4j
public class ImageDTO {

    @Id
    @GeneratedValue(generator = "image_id_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "image_id_gen", sequenceName = "image_id_sequence", initialValue = 900000)
    @Column(name = "image_id")
    private Long id;

    @NonNull
    @OneToOne(mappedBy = "image")
    private Drink drink;

    private byte[] imageBytes;

    private String url;

    public ImageDTO (String imageName) {
        String imageUrl = "src/main/resources/static/" + imageName;
        BufferedImage bImage = null;
        ByteArrayOutputStream bos = null;
        try {
            bImage = ImageIO.read(new File(imageUrl));
            bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        url = imageUrl.toString();
        imageBytes = bos.toByteArray();

        log.info("Created image with url: " + url + ". Saved bytes: " + imageBytes.length + "." );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageDTO imageDTO = (ImageDTO) o;
        return Objects.equals(id, imageDTO.id) && Arrays.equals(imageBytes, imageDTO.imageBytes) && Objects.equals(url, imageDTO.url);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, url);
        result = 31 * result + Arrays.hashCode(imageBytes);
        return result;
    }
}
