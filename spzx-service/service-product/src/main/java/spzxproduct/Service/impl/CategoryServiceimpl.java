package spzxproduct.Service.impl;

import cn.hutool.json.JSONString;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.model.entity.product.Category;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import spzxproduct.Mapper.CategoryMapper;
import spzxproduct.Service.CategoryService;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author 清醒
 * @Date 2024/5/3 15:44
 */
@Service
public class CategoryServiceimpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //获取一级分类
    @Override
    public List<Category> findOneCategory() {
        //查询redis中是否存在一级目录
        String categoryListJSON = stringRedisTemplate.opsForValue().get("category:one");

        if (!StringUtils.isEmpty(categoryListJSON)) {
            //返回redis中的一级目录
            List<Category> oneCategoryList = JSON.parseArray(categoryListJSON, Category.class);
            return oneCategoryList;
        }

        //不存在则去数据库查询同时存入redis
        List<Category> oneCategoryList = categoryMapper.findOneCategory();
        //转化为json格式
        String oneCategory = JSON.toJSONString(oneCategoryList);
        stringRedisTemplate.opsForValue().set("category:one", oneCategory, 7, TimeUnit.DAYS);

        return categoryMapper.findOneCategory();
    }


    //获取分类树形结构
    @Override
    public List<Category> findCategoryTree() {

        //获取所有分类
        List<Category> AllCategoryList = categoryMapper.findAll();

        //获取所有一级分类id根据id查询所有一级分类下的二级分类依次递归查询三级分类
        //longValue()转化为long
        //取出所有一级分类集合
        List<Category> oneCategoryList = AllCategoryList.stream().
                filter(item -> item.getId().longValue() == 0).
                collect(Collectors.toList());

//        //遍历所有分类集合过滤出父节点是一级分类的二级集合
//        oneCategoryList.forEach(oneCategory -> {
//            List<Category> twoCategoryList = AllCategoryList.stream().filter(item -> item.getParentId() == oneCategory.getId()).collect(Collectors.toList());
//            oneCategory.setChildren(twoCategoryList);
//            //将三级分类放入二级分类
//            twoCategoryList.forEach(twoCategory -> {
//                List<Category> threeCategoryList = AllCategoryList.stream().filter(item -> item.getParentId() == twoCategory.getId()).collect(Collectors.toList());
//                twoCategory.setChildren(threeCategoryList);
//            });
//        });
        if (!CollectionUtils.isEmpty(oneCategoryList)) {
            oneCategoryList.forEach(oneCategory -> {
                List<Category> twoCategoryList = AllCategoryList.stream().filter(item -> item.getParentId().longValue() == oneCategory.getId().longValue()).collect(Collectors.toList());
                oneCategory.setChildren(twoCategoryList);

                if (!CollectionUtils.isEmpty(twoCategoryList)) {
                    twoCategoryList.forEach(twoCategory -> {
                        List<Category> threeCategoryList = AllCategoryList.stream().filter(item -> item.getParentId().longValue() == twoCategory.getId().longValue()).collect(Collectors.toList());
                        twoCategory.setChildren(threeCategoryList);
                    });
                }
            });
        }
        //返回树形结构

        return oneCategoryList;
    }
}
