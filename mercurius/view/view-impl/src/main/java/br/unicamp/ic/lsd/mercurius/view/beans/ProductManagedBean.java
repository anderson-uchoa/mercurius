package br.unicamp.ic.lsd.mercurius.view.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import br.unicamp.ic.lsd.mercurius.datatype.Category;
import br.unicamp.ic.lsd.mercurius.datatype.Product;
import br.unicamp.ic.lsd.mercurius.datatype.ProductQuantity;
import br.unicamp.ic.lsd.mercurius.excpetionhandler.exceptions.ProductNotFoundException;
import br.unicamp.ic.lsd.mercurius.view.spec.req.ViewProductMgt;
import br.unicamp.ic.lsd.mercurius.viewproductconnector.ViewProductConnectorFactory;
import br.unicamp.ic.sed.cosmos.IManager;

@Named("productManagedBean")
@SessionScoped
public class ProductManagedBean implements Serializable {

	private static final long serialVersionUID = 3922356213133065938L;

	private Product product;
	private List<Category> categories;
	private ProductQuantity selectedProductQuantity;

	private IManager viewProductConnector;
	private ViewProductMgt productMgt;

	@Inject
	private HttpServletRequest request;

	@PostConstruct
	public void init() {
		viewProductConnector = ViewProductConnectorFactory.createInstance();
		productMgt = (ViewProductMgt) viewProductConnector.getProvidedInterface("ViewProductMgt");
	}

	public void productDetails() throws ProductNotFoundException {
		String idString = request.getParameter("prodId");
		if (NumberUtils.isNumber(idString)) {
			Integer idProduto = Integer.parseInt(idString);
			product = productMgt.getProduct(idProduto);
			if (CollectionUtils.isNotEmpty(product.getCategories())) {
				categories = productMgt.getCategoryParents(product.getCategories().get(0));
			}
			if (CollectionUtils.isNotEmpty(product.getQuantities())) {
				selectedProductQuantity = product.getQuantities().get(0);
			}
		}
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductQuantity getSelectedProductQuantity() {
		return selectedProductQuantity;
	}

	public void setSelectedProductQuantity(ProductQuantity selectedProductQuantity) {
		this.selectedProductQuantity = selectedProductQuantity;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

}