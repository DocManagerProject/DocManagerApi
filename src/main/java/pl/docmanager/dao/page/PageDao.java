package pl.docmanager.dao.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.domain.page.Page;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class PageDao {
    private PageRepository pageRepository;
    private PageValidator pageValidator;

    @Autowired
    public PageDao(PageRepository pageRepository, PageValidator pageValidator) {
        this.pageRepository = pageRepository;
        this.pageValidator = pageValidator;
    }

    public Page getPageByUrl(String url, long solutionId) {
        return pageRepository.findBySolution_IdAndUrl(solutionId, url).orElseThrow(NoSuchElementException::new);
    }

    public void addPage(Page page) {
        pageValidator.validatePage(page);
        pageRepository.save(page);
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
}
