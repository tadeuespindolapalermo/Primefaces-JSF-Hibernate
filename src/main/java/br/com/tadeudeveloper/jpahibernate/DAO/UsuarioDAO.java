package br.com.tadeudeveloper.jpahibernate.DAO;

import java.util.List;

import br.com.tadeudeveloper.jpahibernate.model.UsuarioPessoa;

public class UsuarioDAO<E> extends GenericDAO<UsuarioPessoa> {

	public void removerUsuarioCascata(UsuarioPessoa pessoa) throws Exception {

		// Deleta em cascata - Telefone, E-mail e Usuário

		getEntityManager().getTransaction().begin();

		String sqlDeleteFone = "delete from telefoneuser where usuariopessoa_id = " + pessoa.getId();
		getEntityManager().createNativeQuery(sqlDeleteFone).executeUpdate();

		String sqlDeleteEmail = "delete from email where usuariopessoa_id = " + pessoa.getId();
		getEntityManager().createNativeQuery(sqlDeleteEmail).executeUpdate();

		getEntityManager().getTransaction().commit();
		super.deletarPorId(pessoa);
	}

	public void removerUsuario(UsuarioPessoa pessoa) throws Exception {

		// Deleta em cascata pelo mapeamento da entidade

		getEntityManager().getTransaction().begin();
		getEntityManager().remove(pessoa);
		getEntityManager().getTransaction().commit();
	}

	public List<UsuarioPessoa> pesquisar(String campoPesquisa) {
		return getEntityManager().createQuery("from UsuarioPessoa where nome like '%" + campoPesquisa + "%'", UsuarioPessoa.class).getResultList();
	}

}
