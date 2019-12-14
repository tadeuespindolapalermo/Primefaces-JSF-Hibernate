package br.com.tadeudeveloper.jpahibernate.datatablelazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.tadeudeveloper.jpahibernate.DAO.UsuarioDAO;
import br.com.tadeudeveloper.jpahibernate.model.UsuarioPessoa;

@SuppressWarnings("serial")
public class LazyDataTableModelUsuarioPessoa<T> extends LazyDataModel<UsuarioPessoa> {

	private UsuarioDAO<UsuarioPessoa> usuarioDAO = new UsuarioDAO<>();
	
	public List<UsuarioPessoa> list = new ArrayList<>();
	
	private String sql = " from UsuarioPessoa ";
	
	@Override
	public List<UsuarioPessoa> load(int first, int pageSize, String sortField, 
			SortOrder sortOrder, Map<String, Object> filters) {
		
		list = usuarioDAO.getEntityManager().createQuery(sql, UsuarioPessoa.class)
				.setFirstResult(first)
				.setMaxResults(pageSize)
				.getResultList();
		
		sql = " from UsuarioPessoa ";
		
		setPageSize(pageSize);
		
		Integer qtdRegistro = Integer.parseInt(usuarioDAO.getEntityManager()
				.createQuery("SELECT COUNT(1) " + sql).getSingleResult().toString());
		
		setRowCount(qtdRegistro);
		
		return list;
	}
	
	public List<UsuarioPessoa> getList() {
		return list;
	}
	
	public void pesquisar(String campoPesquisa) {
		sql += " where nome like '%" + campoPesquisa + "%'";
	}

}
