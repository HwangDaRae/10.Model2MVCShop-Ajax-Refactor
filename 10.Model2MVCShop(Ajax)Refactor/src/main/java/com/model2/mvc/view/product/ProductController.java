package com.model2.mvc.view.product;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.CommonUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Upload;
import com.model2.mvc.service.domain.Upload_Sub;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.upload.UploadService;

@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	ProductService productServiceImpl;
	
	@Autowired
	@Qualifier("uploadServiceImpl")
	UploadService uploadServiceImpl;

	public ProductController() {
		System.out.println(getClass() + " default Constructor()]");
		System.out.println("pageSize : " + pageSize);
		System.out.println("pageUnit : " + pageUnit);
	}
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
							  
	@RequestMapping(value = "listProduct/{menu}", method = RequestMethod.GET) /* @RequestParam String menu, */
	public String listProduct( @PathVariable String menu, Model model, HttpSession session, Search search) throws Exception {
		System.out.println("/product/listProduct : GET");
		System.out.println(search);
		System.out.println(menu);
		System.out.println(session.getAttribute("user"));
		
		if(((User)session.getAttribute("user")).getUserId().equals("non-member")) {
			//��ȸ�� ��ǰ �˻� Anchor Tag Ŭ��
			System.out.println("��ȸ������ ���Դ�");
		}else if(((User)session.getAttribute("user")).getRole().equals("admin")) {
			System.out.println("admin�������� ���Դ�");
		}else {
			System.out.println("user�������� ���Դ�");
		}

		// ��ǰ�˻� Ŭ�������� currentPage�� null�̴�
		int currentPage = 1;

		// ��ǰ�˻� Ŭ���� null, �˻���ư Ŭ���� nullString
		if (search.getCurrentPage() != 0) {
			currentPage = search.getCurrentPage();
		}

		// �ǸŻ�ǰ���� Ŭ���� searchKeyword, searchCondition �� �� null ==> nullString ���� ��ȯ
		String searchKeyword = CommonUtil.null2str(search.getSearchKeyword());
		String searchCondition = CommonUtil.null2str(search.getSearchCondition());
		
		// ��ǰ��� ��ǰ���ݿ��� searchKeyword�� �����϶� nullString���� ��ȯ
		if (!searchCondition.trim().equals("1") && !CommonUtil.parsingCheck(searchKeyword)) {
			searchKeyword = "";
		}
		search = new Search(currentPage, searchCondition, searchKeyword, pageSize, search.getPriceSort());
		
		// �˻������� �־ ���� �������� list�� �����´�
		List<Product> prodList = productServiceImpl.getProductList(search);		
		int totalCount = productServiceImpl.getProductTotalCount(search);		
		Page resultPage = new Page(currentPage, totalCount, pageUnit, pageSize);
		
		for (int i = 0; i < prodList.size(); i++) {
			System.out.println(getClass() + " : " + prodList.get(i).toString());
		}
		
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("searchVO", search);
		model.addAttribute("list", prodList);
		model.addAttribute("listSize", prodList.size());
		model.addAttribute("menu", menu);
		
		return "forward:/product/listProduct.jsp";
	}
	
	@RequestMapping( value = "listProduct", method = RequestMethod.POST )
	public String listProduct( @RequestParam("menu") String menu, Model model, User user, HttpSession session, Search search) throws Exception {
		System.out.println("/product/listProduct : POST");
		System.out.println(search);
		System.out.println(user);
		System.out.println(menu);
		System.out.println(session.getAttribute("user"));
		
		if(((User)session.getAttribute("user")).getUserId().equals("non-member")) {
			//��ȸ�� ��ǰ �˻� Anchor Tag Ŭ��
			System.out.println("��ȸ������ ���Դ�");
		}else if(((User)session.getAttribute("user")).getRole().equals("admin")) {
			System.out.println("admin�������� ���Դ�");
		}else {
			System.out.println("user�������� ���Դ�");
		}

		// ��ǰ�˻� Ŭ�������� currentPage�� null�̴�
		int currentPage = 1;

		// ��ǰ�˻� Ŭ���� null, �˻���ư Ŭ���� nullString
		if (search.getCurrentPage() != 0) {
			currentPage = search.getCurrentPage();
		}

		// �ǸŻ�ǰ���� Ŭ���� searchKeyword, searchCondition �� �� null ==> nullString ���� ��ȯ
		String searchKeyword = CommonUtil.null2str(search.getSearchKeyword());
		String searchCondition = CommonUtil.null2str(search.getSearchCondition());
		
		// ��ǰ��� ��ǰ���ݿ��� searchKeyword�� �����϶� nullString���� ��ȯ
		if (!searchCondition.trim().equals("1") && !CommonUtil.parsingCheck(searchKeyword)) {
			searchKeyword = "";
		}
		search = new Search(currentPage, searchCondition, searchKeyword, pageSize, search.getPriceSort());
		
		// �˻������� �־ ���� �������� list�� �����´�
		List<Product> prodList = productServiceImpl.getProductList(search);		
		int totalCount = productServiceImpl.getProductTotalCount(search);		
		Page resultPage = new Page(currentPage, totalCount, pageUnit, pageSize);
		
		for (int i = 0; i < prodList.size(); i++) {
			System.out.println(getClass() + " : " + prodList.get(i).toString());
		}
		
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("searchVO", search);
		model.addAttribute("list", prodList);
		model.addAttribute("listSize", prodList.size());
		model.addAttribute("menu", menu);
		
		return "forward:/product/listProduct.jsp";
	}
	
	@RequestMapping( value = "getProduct/{prodNo}/{menu}", method = RequestMethod.GET )
	public String getProduct(@PathVariable int prodNo, @PathVariable String menu, Model model ) throws Exception {
		System.out.println("/getProduct : GET");
		model.addAttribute("productVO", productServiceImpl.getProduct(prodNo));
		return "forward:/product/getProduct.jsp";
	}
	
	@RequestMapping( value = "addProductView", method = RequestMethod.GET )
	public String addProductView() throws Exception {
		System.out.println("/addProductView : GET");
		return "redirect:/product/addProductView.jsp";
	}
	
	/*
	@RequestMapping(value = "addProduct", method = RequestMethod.POST )
	public String getProduct( @ModelAttribute Product product, Model model) throws Exception {
		System.out.println("/product/addProduct : POST");
		model.addAttribute("productVO", productServiceImpl.addProduct(product));
		return "forward:/product/addProduct.jsp";
	}
	*/
	
	
	/*
	@RequestMapping(value = "addProduct")
	public String getProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("/product/addProduct : POST");

			if (FileUpload.isMultipartContent(request)) {
				String temDir = "C:\\workspace\\07.Model2MVCShop(URI,pattern)Refactor\\src\\main\\webapp\\images\\uploadFiles";

				DiskFileUpload fileUpload = new DiskFileUpload();
				fileUpload.setRepositoryPath(temDir);

				fileUpload.setSizeMax(1024 * 1024 * 10);

				fileUpload.setSizeThreshold(1024 * 100);

				if (request.getContentLength() < fileUpload.getSizeMax()) {
					Product productVO = new Product();

					StringTokenizer token = null;

					List fileItemList = fileUpload.parseRequest(request);
					
					int Size = fileItemList.size();
					System.out.println("size : " + Size);
					for (int i = 0; i < Size; i++) {
						FileItem fileItem = (FileItem) fileItemList.get(i);

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equals("manuDate")) {
								token = new StringTokenizer(fileItem.getString("euc-kr"), "-");
								String manuDate = token.nextToken() + token.nextToken() + token.nextToken();
								productVO.setManuDate(manuDate);
							} else if (fileItem.getFieldName().equals("prodName")) {
								productVO.setProdName(fileItem.getString("euc-kr"));
							} else if (fileItem.getFieldName().equals("prodDetail")) {
								productVO.setProdDetail(fileItem.getString("euc-kr"));
							} else if (fileItem.getFieldName().equals("price")) {
								productVO.setPrice(Integer.parseInt(fileItem.getString("euc-kr")));
							} else if (fileItem.getFieldName().equals("amount")) {
								productVO.setAmount(Integer.parseInt(fileItem.getString("euc-kr")));
							}
						} else { // ���� �����̸�

							if (fileItem.getSize() > 0) {
								int idx = fileItem.getName().lastIndexOf("\\");
								if (idx == -1) {
									idx = fileItem.getName().lastIndexOf("/");
								}
								String fileName = fileItem.getName().substring(idx + 1);
								productVO.setFileName(fileName);

								try {
									File uploadedFile = new File(temDir, fileName);
									fileItem.write(uploadedFile);
								} catch (IOException e) {
									System.out.println(e);
								}

							} else {
								productVO.setFileName("../../images/empty.GIF");
							}

						}//else

					}//for
					
					System.out.println("����� ��ǰ : " + productVO);

					productServiceImpl.addProduct(productVO);
					request.setAttribute("productVO", productVO);

				} else {
					// ���ε��ϴ� ������ setSizeMax���� ū ���
					int overSize = (request.getContentLength() / 1000000);
					System.out.println("<script>alert('������ ũ��� 1MB���� �Դϴ�. �ø��� ���� �뷮��" + overSize + "MB�Դϴ�");
					System.out.println("history.back();</script>");
				}
			} else {
				System.out.println("���ڵ� Ÿ���� multipart/form-data�� �ƴմϴ�.");
			}
		
		return "forward:/product/addProduct.jsp";
	}
	*/
	

	/*
	@RequestMapping(value = "addProduct", method = RequestMethod.POST)
	//public String addProduct(MultipartFile uploadfile, Product productVO, HttpServletRequest request) throws Exception {
	public String addProduct(@ModelAttribute Product productVO, @RequestParam("uploadfile") MultipartFile uploadfile, HttpServletRequest request) throws Exception {
		System.out.println("/product/addProduct : POST");
		
		uploadfile.transferTo(new File("C:\\Users\\bitcamp\\git\\07Model2Refactor\\07.Model2MVCShop(URI,pattern)Refactor\\src\\main\\webapp\\images\\uploadFiles\\", uploadfile.getOriginalFilename()));		
		productVO.setFileName(uploadfile.getOriginalFilename());
		productServiceImpl.addProduct(productVO);		
		request.setAttribute("productVO", productVO);
		
		return "forward:/product/addProduct.jsp";
	}
	*/

	///*
	@RequestMapping(value = "addProduct", method = RequestMethod.POST)
	public String addProduct(@ModelAttribute Product productVO,
							@RequestParam("uploadfile") List<MultipartFile> multiFileList,
							HttpServletRequest request,
							Upload uploadVO,
							Upload_Sub uploadSubVO,
							ArrayList<String> fileName ) throws Exception {
		System.out.println("/product/addProduct : POST");
		
		
		// File file = new File(��� + �����̸�);
		for(int i = 0; i < multiFileList.size(); i++) {
			multiFileList.get(i).transferTo(new File("C:\\Users\\bitcamp\\git\\07Model2Refactor\\07.Model2MVCShop(URI,pattern)Refactor\\src\\main\\webapp\\images\\uploadFiles\\",
					multiFileList.get(i).getOriginalFilename()));
		}
		
		int size = multiFileList.size();
		switch (size) {
		case 1:
			uploadSubVO.setFileName1(multiFileList.get(0).getOriginalFilename());
			break;
		case 2:
			uploadSubVO.setFileName1(multiFileList.get(0).getOriginalFilename());
			uploadSubVO.setFileName2(multiFileList.get(1).getOriginalFilename());
			break;
		case 3:
			uploadSubVO.setFileName1(multiFileList.get(0).getOriginalFilename());
			uploadSubVO.setFileName2(multiFileList.get(1).getOriginalFilename());
			uploadSubVO.setFileName3(multiFileList.get(2).getOriginalFilename());
			break;
		case 4:
			uploadSubVO.setFileName1(multiFileList.get(0).getOriginalFilename());
			uploadSubVO.setFileName2(multiFileList.get(1).getOriginalFilename());
			uploadSubVO.setFileName3(multiFileList.get(2).getOriginalFilename());
			uploadSubVO.setFileName4(multiFileList.get(3).getOriginalFilename());
			break;
		case 5:
			uploadSubVO.setFileName1(multiFileList.get(0).getOriginalFilename());
			uploadSubVO.setFileName2(multiFileList.get(1).getOriginalFilename());
			uploadSubVO.setFileName3(multiFileList.get(2).getOriginalFilename());
			uploadSubVO.setFileName4(multiFileList.get(3).getOriginalFilename());
			uploadSubVO.setFileName5(multiFileList.get(4).getOriginalFilename());
			break;
		}
		
		//������ȣ ����
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileNo = sdf1.format( Calendar.getInstance().getTime() ) + "";
		
		uploadVO.setFileNo(fileNo);
		uploadVO.setFileName(fileName);
		uploadVO.setFileCount(multiFileList.size());
		
		System.out.println("uploadVO : " + uploadVO);
		System.out.println("uploadSubVO : " + uploadSubVO);
		
		productVO.setFileName(fileNo);
		///*
		productServiceImpl.addProduct(productVO);
		uploadServiceImpl.addUpload(uploadVO, uploadSubVO);
		
		request.setAttribute("productVO", productVO);
		request.setAttribute("uploadVO", uploadVO);
		request.setAttribute("count", uploadVO.getFileCount());
		//*/
		
		return "forward:/product/addProduct.jsp";
	}
	//*/
	
	@RequestMapping(value = "updateProductView/{prodNo}/{menu}", method = RequestMethod.GET )
	public String updateProductView(@PathVariable int prodNo, @PathVariable String menu, Model model) throws Exception {
		System.out.println("/product/updateProductView : GET");
		System.out.println("prodNo : " + prodNo);
		System.out.println("menu : " + menu);
		
		Product productVO = productServiceImpl.getProduct(prodNo);
		Upload uploadVO = uploadServiceImpl.getUploadFile(productVO.getFileName());
		
		System.out.println("uploadVO : " + uploadVO);
		
		model.addAttribute("productVO", productVO);
		model.addAttribute("uploadVO", uploadVO);
		model.addAttribute("count", uploadVO.getFileCount());
		
		if( menu.equals("manage") && productVO.getProTranCode() == null ) {
			return "forward:/product/updateProductView.jsp";
		}else {
			return "forward:/product/getProduct.jsp";
		}
	}
	
	/*
	@RequestMapping(value = "updateProduct", method = RequestMethod.POST )
	public String updateProduct(@ModelAttribute("productVO") Product productVO, Model model) throws Exception {
		System.out.println("/product/updateProduct : POST");
		
		model.addAttribute("productVO", productServiceImpl.updateProduct(productVO));
		
		return "forward:/product/getProduct.jsp";
	}
	*/
	@RequestMapping(value = "updateProduct", method = RequestMethod.POST )
	public String updateProduct(@ModelAttribute("productVO") Product productVO,
								@RequestParam("uploadfile") List<MultipartFile> multiFileList,
								HttpServletRequest request,
								Upload uploadVO,
								Upload_Sub uploadSubVO ) throws Exception {
		System.out.println("/product/updateProduct : POST");
		System.out.println("������Ʈ �� ��ǰ ���� : " + productVO);
		System.out.println("���� ����Ʈ ������ : " + multiFileList.size());
		
		// File file = new File(��� + �����̸�);
		for(int i = 0; i < multiFileList.size(); i++) {
			multiFileList.get(i).transferTo(new File("C:\\Users\\bitcamp\\git\\07Model2Refactor\\07.Model2MVCShop(URI,pattern)Refactor\\src\\main\\webapp\\images\\uploadFiles\\",
					multiFileList.get(i).getOriginalFilename()));
		}
		
		int size = multiFileList.size();
		System.out.println("������Ʈ �� ���� ���� : " + size);
		
		switch (size) {
		case 1:
			uploadSubVO.setFileName1(multiFileList.get(0).getOriginalFilename());
			break;
		case 2:
			uploadSubVO.setFileName1(multiFileList.get(0).getOriginalFilename());
			uploadSubVO.setFileName2(multiFileList.get(1).getOriginalFilename());
			break;
		case 3:
			uploadSubVO.setFileName1(multiFileList.get(0).getOriginalFilename());
			uploadSubVO.setFileName2(multiFileList.get(1).getOriginalFilename());
			uploadSubVO.setFileName3(multiFileList.get(2).getOriginalFilename());
			break;
		case 4:
			uploadSubVO.setFileName1(multiFileList.get(0).getOriginalFilename());
			uploadSubVO.setFileName2(multiFileList.get(1).getOriginalFilename());
			uploadSubVO.setFileName3(multiFileList.get(2).getOriginalFilename());
			uploadSubVO.setFileName4(multiFileList.get(3).getOriginalFilename());
			break;
		case 5:
			uploadSubVO.setFileName1(multiFileList.get(0).getOriginalFilename());
			uploadSubVO.setFileName2(multiFileList.get(1).getOriginalFilename());
			uploadSubVO.setFileName3(multiFileList.get(2).getOriginalFilename());
			uploadSubVO.setFileName4(multiFileList.get(3).getOriginalFilename());
			uploadSubVO.setFileName5(multiFileList.get(4).getOriginalFilename());
			break;
		}

		System.out.println("������Ʈ �� ��ǰ��ȣ : " + productVO.getProdNo());
		uploadVO.setFileNo(productVO.getFileName());
		uploadVO.setFileCount(multiFileList.size());
		
		System.out.println("uploadVO : " + uploadVO);
		System.out.println("uploadSubVO : " + uploadSubVO);
		
		productVO = productServiceImpl.updateProduct(productVO);
		uploadVO = uploadServiceImpl.updateUpload(uploadVO, uploadSubVO);
		
		request.setAttribute("productVO", productVO);
		request.setAttribute("uploadVO", uploadVO);
		request.setAttribute("count", uploadVO.getFileCount());
		
		return "forward:/product/getProduct.jsp";
	}

}



























