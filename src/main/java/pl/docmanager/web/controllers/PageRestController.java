package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.dao.page.PageDao;
import pl.docmanager.domain.page.Page;

import java.util.Map;

@RestController
public class PageRestController extends RestControllerBase {

    private PageDao pageDao;

    @Autowired
    public PageRestController(PageDao pageDao) {
        this.pageDao = pageDao;
    }

    @GetMapping("/api/pages/solution/{solutionId}/url/{url}")
    public Page getPageByUrl(@PathVariable("solutionId") long solutionId,
                             @PathVariable("url") String url,
                             @RequestHeader("apiToken") String apiToken) {
        return pageDao.getPageByUrl(url, solutionId, apiToken);
    }

    @PostMapping("/api/pages")
    public void addPage(@RequestBody Page page, @RequestHeader("apiToken") String apiToken) {
        pageDao.addPage(page, apiToken);
    }

    @PatchMapping("/api/pages/solution/{solutionId}/url/{url}")
    public void updatePage(@RequestBody Map<String, Object> updatesMap,
                           @PathVariable("solutionId") long solutionId,
                           @PathVariable("url") String url,
                           @RequestHeader("apiToken") String apiToken) {
        pageDao.updatePage(updatesMap, url, solutionId, apiToken);
    }
}
