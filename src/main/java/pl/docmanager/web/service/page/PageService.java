package pl.docmanager.web.service.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.dao.page.PageDao;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.page.PageState;
import pl.docmanager.domain.page.wrapper.PageWithCategories;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PageService {

    private AccessValidator accessValidator;
    private ApiTokenDecoder apiTokenDecoder;
    private PageDao pageDao;

    @Autowired
    public PageService(AccessValidator accessValidator, ApiTokenDecoder apiTokenDecoder,
                       PageDao pageDao) {
        this.accessValidator = accessValidator;
        this.apiTokenDecoder = apiTokenDecoder;
        this.pageDao = pageDao;
    }

    public Page getPageByUrl(String url, long solutionId, String apiToken) {
        User requester = apiTokenDecoder.getUseFromApiToken(apiToken);
        accessValidator.validateSolution(requester, solutionId);
        return pageDao.getPageByUrl(url, solutionId);
    }

    public void addPage(PageWithCategories pageWithCategories, String apiToken) {
        User requester = apiTokenDecoder.getUseFromApiToken(apiToken);
        Page page = pageWithCategories.getPage();
        List<Long> categoriesIds = pageWithCategories.getCategories();

        if (page == null) {
            throw new IllegalArgumentException("Page cannot be null");
        }

        if (categoriesIds == null) {
            throw new IllegalArgumentException("CategoriesIDs cannot be null");
        }

        if (page.getSolution() == null) {
            throw new IllegalArgumentException("Page's solution cannot be null");
        }
        accessValidator.validateSolution(requester, page.getSolution().getId());
        page.setAuthor(requester);
        page.setCreateDate(LocalDateTime.now());
        page.setState(PageState.ACTIVE);
        page = pageDao.addPage(page);
        pageDao.addPageToCategories(page, categoriesIds);
    }

    public Page updatePage(Map<String, Object> updatesMap, String url, long solutionId, String apiToken) {
        User requester = apiTokenDecoder.getUseFromApiToken(apiToken);
        accessValidator.validateSolution(requester, solutionId);
        return pageDao.updatePage(updatesMap, url, solutionId);
    }
}
