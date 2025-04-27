package projet.scrapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private InfoSiteService infoSiteService;

    @Override
    public void run(String... args) throws Exception {
        // Vérifier si les sites existent déjà dans la base
        if (infoSiteService.getAllInfoSites().isEmpty()) {
            // Ajouter les sites
            infoSiteService.addInfoSite(new InfoSite("amazon", "https://www.amazon.fr/s?k=", "a.a-link-normal", "div.puis-card-container", "h2.a-size-base-plus", "span.a-price", "span.a-price", "div.celwidget ul.a-unordered-list > li.a-spacing-mini", "div.imgTagWrapper img"));
            infoSiteService.addInfoSite(new InfoSite("cdiscount", "https://www.cdiscount.com/search/10/ .html", "div a.o-card__link", "article.o-card", "h4.o-card__title", "div.prdtBILPrice", "div.c-buybox__price span.u-visually-hidden", "dl.c-productHighlights__list", "div.u-ratio img"));
            infoSiteService.addInfoSite(new InfoSite("lego", "https://www.lego.com/fr-fr/search?q=", "a.ProductImage_productLink__G_6o_", "li.Grid_grid-item__FLJlN", "h3.ProductLeaf_titleRow__KqWbB", "span.ds-label-md-bold", "span.ds-heading-lg", "span.Markup__StyledMarkup-sc-nc8x20-0 p", "div.ProductGallerystyles__MediaWrapper-sc-1uy048w-1 source"));
            infoSiteService.addInfoSite(new InfoSite("rueducommerce", "https://www.rueducommerce.fr/recherche/", "a.stretched-link", "li.pdt-item", "h3.title-3", "div.price", "div.price", "div.description", "div.product-image img"));
            infoSiteService.addInfoSite(new InfoSite("ldlc", "https://www.ldlc.com/recherche/", "h3.title-3 a", "li.pdt-item", "h3.title-3", "div.price", "div.price", "div.description p.desc", "div.product img"));
            infoSiteService.addInfoSite(new InfoSite("boulanger", "https://www.boulanger.com/resultats?tr=", "div.product-list__product-area-1 a", "li.product-list__item", "h2.product-list__product-label", "div.price p", "div.block_price p", "div.keypoints  ul", "div.product__sticky source"));
            infoSiteService.addInfoSite(new InfoSite("lidl", "https://www.lidl.fr/q/search?q=", "a.ods-image-gallery__items", "li.s-grid__item", "div.product-grid-box__title", "div.m-price__price", "div.m-price__bottom", "div.info__content--legacy-de-styles ul", "div.detail__column img"));
            infoSiteService.addInfoSite(new InfoSite("alternate", "https://www.alternate.fr/listing.xhtml?q=", "a.card", "div.grid-container a", "div.product-name", "span.price", "span.price ", "div.card-body", "div.swiper-slide img"));
            infoSiteService.addInfoSite(new InfoSite("ebay", "https://www.ebay.fr/sch/i.html?_nkw=", "div.s-item__image a", "li.s-item", "div.s-item__title", "span.s-item__price", "div.x-price-primary span", "div.vim div.ux-layout-section-evo__item", "div.ux-image-carousel-item img"));
            infoSiteService.addInfoSite(new InfoSite("fnac", "https://www.fnac.com/SearchResult/ResultList.aspx?Search=", "div.Article-infoContent a", "div.clearfix", "a.Article-title", "div.floatl", "span.f-faPriceBox__price", "div.f-productDesc__raw p", "section.f-productMedias img"));
            infoSiteService.addInfoSite(new InfoSite("leclerc", "https://www.e.leclerc/recherche?q=", "li.px-0 a", "li.px-0", "a.mb-0", "div.price", "div.price", "span.mb-0", "div.product-block-preview img"));
        }
    }
}