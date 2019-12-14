package br.com.tadeudeveloper.jpahibernate;

import java.util.List;

import org.junit.Test;

import br.com.tadeudeveloper.jpahibernate.DAO.GenericDAO;
import br.com.tadeudeveloper.jpahibernate.model.TelefoneUser;
import br.com.tadeudeveloper.jpahibernate.model.UsuarioPessoa;
import br.com.tadeudeveloper.jpahibernate.util.HibernateUtil;

@SuppressWarnings("unchecked")
public class TestHibernate {
	
	@Test
	public void testHibernateUtil() {
		HibernateUtil.geEntityManager();		
	}
	
	@Test
	public void testSalvar() {			
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();		
		UsuarioPessoa pessoa = new UsuarioPessoa();		
		pessoa.setIdade(45);
		pessoa.setNome("Tadeu 2");
		pessoa.setSenha("123");
		pessoa.setLogin("tadeuespindolapalermo");
		pessoa.setSobrenome("Espíndola Palermo");		
		genericDAO.salvar(pessoa);				
	}
	
	@Test
	public void testBuscar() {
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();
		UsuarioPessoa pessoa = new UsuarioPessoa();
		pessoa.setId(1L);
		pessoa = genericDAO.pesquisar(pessoa);		
		System.out.println(pessoa);
	}
	
	@Test
	public void testBuscarPorId() {
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();		
		UsuarioPessoa pessoa = genericDAO.pesquisar(1L, UsuarioPessoa.class);		
		System.out.println(pessoa);
	}
	
	@Test
	public void testUpdate() {
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();		
		UsuarioPessoa pessoa = genericDAO.pesquisar(2L, UsuarioPessoa.class);	
		pessoa.setIdade(99);
		pessoa.setNome("Antonio");
		pessoa.setSenha("123456");
		pessoa.setLogin("antony");
		pessoa.setSobrenome("Francisco dos Santos");			
		pessoa = genericDAO.updateMerge(pessoa);
		System.out.println(pessoa);
	}
	
	@Test
	public void testDelete() {
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();		
		UsuarioPessoa pessoa = genericDAO.pesquisar(1L, UsuarioPessoa.class);	
		try {
			genericDAO.deletarPorId(pessoa);
		} catch (Exception e) {			
			e.printStackTrace();
		}		
	}
	
	@Test
	public void testCosultarTodos() {
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();		
		List<UsuarioPessoa>	list = genericDAO.listar(UsuarioPessoa.class);
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
			System.out.println();
		}
	}
	
	@Test
	public void testQueryList() {
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();
		List<UsuarioPessoa> list = genericDAO.getEntityManager().createQuery("from UsuarioPessoa where nome = 'Antonio'").getResultList();		
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}
	
	@Test
	public void testQueryListMaxResult() {
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();
		List<UsuarioPessoa> list = genericDAO.getEntityManager().createQuery("from UsuarioPessoa order by nome")
				.setMaxResults(2)
				.getResultList();		
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}
	
	@Test
	public void testeQueryListParameter() {
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();
		List<UsuarioPessoa> list = genericDAO.getEntityManager().createQuery("from UsuarioPessoa where nome = :nome or sobrenome = :sobrenome")
				.setParameter("nome", "Antonio")
				.setParameter("sobrenome", "Francisco dos Santos")
				.getResultList();		
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}	
	
	@Test
	public void testeQuerySomaIdade() {
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();
		Long somaIdade = (Long) genericDAO.getEntityManager()
				.createQuery("select sum(u.idade) from UsuarioPessoa u").getSingleResult();
		System.out.println("Soma de todas as idades é: " + somaIdade);
		
		Double mediaIdade = (Double) genericDAO.getEntityManager()
				.createQuery("select avg(u.idade) from UsuarioPessoa u").getSingleResult();
		System.out.println("Média de todas as idades é: " + mediaIdade);
	}	
	
	@Test
	public void testNamedQueryTodos() {
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();
		List<UsuarioPessoa> list = genericDAO.getEntityManager()
				.createNamedQuery("UsuarioPessoa.todos")
				.getResultList();		
		for(UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}
	
	@Test
	public void testNamedQueryNome() {
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();
		List<UsuarioPessoa> list = genericDAO.getEntityManager()
				.createNamedQuery("UsuarioPessoa.buscaPorNome")
				.setParameter("nome", "Tadeu 2")
				.getResultList();		
		for(UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testGravarTelefone() {
		GenericDAO genericDAO = new GenericDAO();
		UsuarioPessoa pessoa = (UsuarioPessoa) genericDAO.pesquisar(14L, UsuarioPessoa.class);
		TelefoneUser telefoneUser = new TelefoneUser();
		telefoneUser.setTipo("Casa");
		telefoneUser.setNumero("(61) 3397-5465");
		telefoneUser.setUsuarioPessoa(pessoa);
		genericDAO.salvar(telefoneUser);
	}
	
	@Test
	public void testConsultaTelefone() {
		GenericDAO<UsuarioPessoa> genericDAO = new GenericDAO<>();
		UsuarioPessoa pessoa = (UsuarioPessoa) genericDAO.pesquisar(3L, UsuarioPessoa.class);
		for (TelefoneUser fone : pessoa.getTelefoneUsers()) {
			System.out.println(fone.getNumero());
			System.out.println(fone.getTipo());
			System.out.println(fone.getUsuarioPessoa().getNome());
			System.out.println("--------------------");
		}
	}
	
}
