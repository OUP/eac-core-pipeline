package com.oup.eac.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Product;

public class CustomerWithLicencesAndProductsDto implements Serializable {

	private Customer customer;
	private final List<LicenceDto> licences;
	private final Map<LicenceDto, List<Product>> licenceProducts;
	
	
	public CustomerWithLicencesAndProductsDto(Customer customer,
			List<LicenceDto> licences,
			Map<LicenceDto, List<Product>> licenceProducts) {
		super();
		this.customer = customer;
		this.licences = licences;
		this.licenceProducts = licenceProducts;
	}

	public Customer getCustomer() {
		return customer;
	}

	public List<LicenceDto> getLicences() {
		return licences;
	}

	public Map<LicenceDto, List<Product>> getLicenceProducts() {
		return licenceProducts;
	}
}
