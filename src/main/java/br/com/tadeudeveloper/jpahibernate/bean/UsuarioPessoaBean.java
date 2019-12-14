package br.com.tadeudeveloper.jpahibernate.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import br.com.tadeudeveloper.jpahibernate.DAO.EmailDAO;
import br.com.tadeudeveloper.jpahibernate.DAO.UsuarioDAO;
import br.com.tadeudeveloper.jpahibernate.datatablelazy.LazyDataTableModelUsuarioPessoa;
import br.com.tadeudeveloper.jpahibernate.model.Email;
import br.com.tadeudeveloper.jpahibernate.model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaBean")
@ViewScoped
public class UsuarioPessoaBean implements Serializable {		
	
	private static final long serialVersionUID = 1L;
	
	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	
	//private List<UsuarioPessoa> listUsuarioPessoa = new ArrayList<>(); // SEM PAGINATOR
	private LazyDataTableModelUsuarioPessoa<UsuarioPessoa> listUsuarioPessoa = new LazyDataTableModelUsuarioPessoa<>();
	
	private UsuarioDAO<UsuarioPessoa> usuarioDAO = new UsuarioDAO<>();
	private EmailDAO<Email> emailDAO = new EmailDAO<>();
	private BarChartModel barChartModel = new BarChartModel();
	private Boolean novoUsuario;
	private Email email = new Email();
	private String campoPesquisa;
	
	@PostConstruct
	public void init() {
		//listUsuarioPessoa = usuarioDAO.listar(UsuarioPessoa.class); // SEM PAGINATOR
		listUsuarioPessoa.load(0, 5, null, null, null);
		novoUsuario = true;			
		montarGrafico();
	}

	private void montarGrafico() {
		barChartModel = new BarChartModel();
		ChartSeries userSalario = new ChartSeries(); 	// Grupo de funcionários		
		//for (UsuarioPessoa usuarioPessoa : listUsuarioPessoa) { // add salário para o grupo // SEM PAGINATOR
		for (UsuarioPessoa usuarioPessoa : listUsuarioPessoa.list) { // add salário para o grupo
			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario()); // add salários
		}
		barChartModel.addSeries(userSalario); // add o grupo
		barChartModel.setTitle("Gráfico de Salários");
	}
	
	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + usuarioPessoa.getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			
			UsuarioPessoa userCepPessoa = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);
			
			usuarioPessoa.setCep(userCepPessoa.getCep());
			usuarioPessoa.setLogradouro(userCepPessoa.getLogradouro());
			usuarioPessoa.setComplemento(userCepPessoa.getComplemento());
			usuarioPessoa.setBairro(userCepPessoa.getBairro());
			usuarioPessoa.setLocalidade(userCepPessoa.getLocalidade());
			usuarioPessoa.setUf(userCepPessoa.getUf());
			usuarioPessoa.setUnidade(userCepPessoa.getUnidade());
			usuarioPessoa.setIbge(userCepPessoa.getIbge());
			usuarioPessoa.setGia(userCepPessoa.getGia());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public void addEmail() {
		email.setUsuarioPessoa(usuarioPessoa);
		email = emailDAO.updateMerge(email);
		usuarioPessoa.getEmails().add(email);
		email = new Email();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
			FacesMessage.SEVERITY_INFO, "Sucesso", "E-mail salvo com sucesso!"));
	}
	
	public void removerEmail() throws Exception {
		String idEmail = FacesContext.getCurrentInstance().getExternalContext()
			.getRequestParameterMap().get("idEmail");
		Email email = new Email();
		email.setId(Long.parseLong(idEmail));
		emailDAO.deletarPorId(email);
		usuarioPessoa.getEmails().remove(email);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Sucesso", "E-mail removido com sucesso!"));
	}
	
	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}
	
	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}
	
	public String salvar() {
		usuarioDAO.salvar(usuarioPessoa);
		if (novoUsuario)
			//listUsuarioPessoa.add(usuarioPessoa); // SEM PAGINATOR
			listUsuarioPessoa.list.add(usuarioPessoa);
		novoUsuario = false;
		usuarioPessoa = new UsuarioPessoa();
		init();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com sucesso!"));
		return "";
	}
	
	public String novo() {
		usuarioPessoa = new UsuarioPessoa();
		novoUsuario = true;
		return "";
	}
	
	public String editar() {		
		novoUsuario = false;
		return "";
	}
	
	public String atualizarGrafico(String outcome) {
		return outcome + "?faces-redirect?true";
	}
	
	//public List<UsuarioPessoa> getListUsuarioPessoa() { // SEM PAGINATOR
	public LazyDataTableModelUsuarioPessoa<UsuarioPessoa> getListUsuarioPessoa() {		
		montarGrafico();
		return listUsuarioPessoa;
	}
	
	public String remover() {
		try {
			usuarioDAO.removerUsuario(usuarioPessoa);
			//listUsuarioPessoa.remove(usuarioPessoa); // SEM PAGINATOR
			listUsuarioPessoa.list.remove(usuarioPessoa);
			usuarioPessoa = new UsuarioPessoa();
			init();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Removido com sucesso!"));
			
		} catch (Exception e) {
			if(e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Não pode ser removido! Existem telefones para o usuário!");
			} else {
				e.printStackTrace();
			}
		}
		return "";		
	}
	
	public void pesquisar() {
		//listUsuarioPessoa = usuarioDAO.pesquisar(campoPesquisa); // SEM PAGINATOR
		listUsuarioPessoa.pesquisar(campoPesquisa);
		montarGrafico();
	}
	
	public void upload(FileUploadEvent image) {
		String imagem = "data:image/png;base64," + DatatypeConverter.printBase64Binary(image.getFile().getContents());
		usuarioPessoa.setImagem(imagem);
	}
	
	public void download() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownloadId = params.get("fileDownloadId");
		
		UsuarioPessoa pessoa = usuarioDAO.pesquisar(Long.parseLong(fileDownloadId), UsuarioPessoa.class);		
		
		byte[] imagem = Base64.decodeBase64(pessoa.getImagem().split("\\,")[1]);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		response.addHeader("Content-Disposition", "attachment; filename=download.png");
		response.setContentType("application/octet-stream");
		response.setContentLength(imagem.length);
		response.getOutputStream().write(imagem);
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
	}

	public BarChartModel getBarChartModel() {
		return barChartModel;
	}

	public void setBarChartModel(BarChartModel barChartModel) {
		this.barChartModel = barChartModel;
	}

	public Boolean getNovoUsuario() {
		return novoUsuario;
	}

	public void setNovoUsuario(Boolean novoUsuario) {
		this.novoUsuario = novoUsuario;
	}		
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	public Email getEmail() {
		return email;
	}
	
	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}
	
	public String getCampoPesquisa() {
		return campoPesquisa;
	}
	
}
