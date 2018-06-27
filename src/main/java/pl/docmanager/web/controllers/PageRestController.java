package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.page.wrapper.PageWithCategories;
import pl.docmanager.web.service.page.PageService;

import java.util.Map;

@RestController
public class PageRestController extends RestControllerBase {

    private PageService pageService;

    @Autowired
    public PageRestController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/api/pages/solution/{solutionId}/url/{url}")
    public Page getPageByUrl(@PathVariable("solutionId") long solutionId,
                             @PathVariable("url") String url,
                             @RequestHeader("apiToken") String apiToken) {
        return pageService.getPageByUrl(url, solutionId, apiToken);
    }

    @PostMapping("/api/pages")
    public void addPage(@RequestBody PageWithCategories pageWithCategories, @RequestHeader("apiToken") String apiToken) {
        pageService.addPage(pageWithCategories, apiToken);
    }

    @PatchMapping("/api/pages/solution/{solutionId}/url/{url}")
    public void updatePage(@RequestBody Map<String, Object> updatesMap,
                           @PathVariable("solutionId") long solutionId,
                           @PathVariable("url") String url,
                           @RequestHeader("apiToken") String apiToken) {
        pageService.updatePage(updatesMap, url, solutionId, apiToken);
    }
}
