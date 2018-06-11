package pl.docmanager.dao.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.dao.PageSectionRepository;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.page.PageSection;
import pl.docmanager.domain.page.PageSectionState;
import pl.docmanager.domain.page.PageState;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PageDao {

    private AccessValidator accessValidator;
    private ApiTokenDecoder apiTokenDecoder;
    private PageRepository pageRepository;
    private PageValidator pageValidator;
    private PageSectionRepository pageSectionRepository;

    @Autowired
    public PageDao(AccessValidator accessValidator, ApiTokenDecoder apiTokenDecoder,
                   PageRepository pageRepository, PageValidator pageValidator,
                   PageSectionRepository pageSectionRepository) {
        this.accessValidator = accessValidator;
        this.apiTokenDecoder = apiTokenDecoder;
        this.pageRepository = pageRepository;
        this.pageValidator = pageValidator;
        this.pageSectionRepository = pageSectionRepository;
    }

    public Page getPageByUrl(String url, long solutionId, String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        Optional<Page> optPage = pageRepository.findBySolution_IdAndUrl(solutionId, url);

        if (optPage.isPresent()) {
            Page page = optPage.get();
            accessValidator.validateSolution(user, page.getSolution().getId());
            return page;
        }
        throw new NoSuchElementException();
    }

    public void addPage(Page page, String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        pageValidator.validatePage(page);
        accessValidator.validateSolution(user, page.getSolution().getId());
        page.setAuthor(user);
        page.setCreateDate(LocalDateTime.now());
        page.setState(PageState.ACTIVE);

        List<PageSection> sections = page.getSections();
        for (PageSection pageSection : sections) {
            pageSection.setPage(page);
            pageSection.setState(PageSectionState.ACTIVE);
        }
        pageRepository.save(page);
        pageSectionRepository.saveAll(sections);
    }

    public Page updatePage(Map<String, Object> updatesMap, String url, long solutionId, String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        accessValidator.validateSolution(user, solutionId);
        Optional<Page> existingPageOpt = pageRepository.findBySolution_IdAndUrl(solutionId, url);

        if (!existingPageOpt.isPresent()) {
            throw new NoSuchElementException();
        }

        Page existingPage = existingPageOpt.get();
        pageValidator.validateLegalUpdate(user, existingPage, updatesMap);
        if (updatesMap.containsKey("name")) {
            existingPage.setName(updatesMap.get("name").toString());
        }

        if (updatesMap.containsKey("url")) {
            existingPage.setUrl(updatesMap.get("url").toString());
        }

        // TODO add sections update
        pageRepository.save(existingPage);
        return existingPage;
    }
}
