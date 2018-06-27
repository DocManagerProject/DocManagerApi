package pl.docmanager.dao.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.dao.category.CategoryItemDao;
import pl.docmanager.domain.category.CategoryBuilder;
import pl.docmanager.domain.category.CategoryItem;
import pl.docmanager.domain.category.CategoryItemBuilder;
import pl.docmanager.domain.category.CategoryItemContentType;
import pl.docmanager.domain.page.Page;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PageDao {
    private PageRepository pageRepository;
    private PageValidator pageValidator;
    private CategoryItemDao categoryItemDao;

    @Autowired
    public PageDao(PageRepository pageRepository, PageValidator pageValidator, CategoryItemDao categoryItemDao) {
        this.pageRepository = pageRepository;
        this.pageValidator = pageValidator;
        this.categoryItemDao = categoryItemDao;
    }

    public Page getPageByUrl(String url, long solutionId) {
        return pageRepository.findBySolution_IdAndUrl(solutionId, url).orElseThrow(NoSuchElementException::new);
    }

    public Page addPage(Page page) {
        pageValidator.validatePage(page);
        return pageRepository.save(page);
    }

    public Page updatePage(Map<String, Object> updatesMap, String url, long solutionId) {
        Page existingPage = pageRepository.findBySolution_IdAndUrl(solutionId, url).orElseThrow(NoSuchElementException::new);
        pageValidator.validateLegalUpdate(updatesMap);

        if (updatesMap.containsKey("name")) {
            existingPage.setName(updatesMap.get("name").toString());
        }

        if (updatesMap.containsKey("content")) {
            existingPage.setContent(updatesMap.get("content").toString());
        }

        if (updatesMap.containsKey("url")) {
            existingPage.setUrl(updatesMap.get("url").toString());
        }

        return pageRepository.save(existingPage);
    }

    public void addPageToCategories(Page page, List<Long> categoriesIds) {
        if (page == null) {
            throw new IllegalArgumentException("Page cannot be null");
        }

        if (categoriesIds == null) {
            throw new IllegalArgumentException("CategoriesIDs cannot be null");
        }

        List<CategoryItem> currentCategoryItems = categoryItemDao.getAllByContentPageId(page.getId());
        List<Long> currentCategoryItemsCategoryIds = currentCategoryItems.stream()
                .map(x -> x.getCategory().getId())
                .collect(Collectors.toList());
        List<CategoryItem> toRemove = currentCategoryItems.stream()
                .filter(x -> !categoriesIds.contains(x.getCategory().getId()))
                .collect(Collectors.toList());
        List<CategoryItem> toAdd = categoriesIds.stream()
                .filter(x -> !currentCategoryItemsCategoryIds.contains(x))
                .map(x -> new CategoryItemBuilder(0, new CategoryBuilder(x, null).build())
                            .withContentType(CategoryItemContentType.PAGE)
                            .withContentPage(page)
                            .withIndex(0) // TODO: change it
                            .build()
                ).collect(Collectors.toList());
        categoryItemDao.removeAll(toRemove);
        categoryItemDao.addAll(toAdd);
    }
}
