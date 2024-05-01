package SPZX.Service;

import com.atguigu.spzx.model.entity.product.Category;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/3/19 18:57
 */
public interface CategoryService {
    List<Category> fingcategoryById(Long id);

    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);
}
