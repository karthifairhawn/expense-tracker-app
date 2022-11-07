package api.v1.dao.transactions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import api.v1.entity.transactions.Expense;
import api.v1.exception.CustomException;
import api.v1.utils.DatabaseUtil;

public class ExpenseTransactionsDaoService {

	
	private ExpenseTransactionsDaoService() {}
	private static ExpenseTransactionsDaoService expenseTransactionsDaoService;
	DatabaseUtil dbUtil;
	public static ExpenseTransactionsDaoService getInstance(){
		if(expenseTransactionsDaoService == null){
			expenseTransactionsDaoService = new ExpenseTransactionsDaoService();
			expenseTransactionsDaoService.dbUtil = DatabaseUtil.getInstance();
		}
		return expenseTransactionsDaoService;
	}

	
	public Expense save(Expense expense) {
		try {
			String spendOn = expense.getSpendOn();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss aa");
		    Date parsedDate = dateFormat.parse(spendOn);
		    Timestamp spendOnTimestamp = new Timestamp(parsedDate.getTime());
			
			Long categoryId = expense.getCategoryId();
			String reason = expense.getReason();
			String note = expense.getNote();
			Long transactionId = expense.getTransactionId();
			
			String sql = "INSERT INTO `expenses`(`spend_on`, `category_id`, `reason`, `note`, `transaction_id`) VALUES ('"+spendOnTimestamp+"','"+categoryId+"','"+reason+"','"+note+"','"+transactionId+"')";
			ResultSet rs = dbUtil.executeInsertionQuery(sql);
			rs.next();
			Long expenseId = rs.getLong(1);
			expense.setId(expenseId);
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("Creation of expense transaction failed, Halted in semi stage",500,new Date().toLocaleString());
		}
		return expense;
	}

	public Expense findByTransactionId(Long transactionId) {
		ResultSet rs;
		
		Expense expense = new Expense();
		String sql = "SELECT * FROM `expenses` WHERE transaction_id = "+transactionId;
		
		try {
			rs = dbUtil.executeSelectionQuery(sql);

			while (rs.next()) {
				expense.setId(rs.getLong("id"));
				expense.setCategoryId(rs.getLong("category_id"));
				expense.setNote(rs.getString("note"));
				expense.setReason(rs.getString("reason"));
				expense.setSpendOn(new Timestamp(rs.getLong("spend_on")).toString());
				expense.setTransactionId(transactionId);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CustomException("Problem in retriving your transaction", 500,new Date().toLocaleString());
		}
		
		return expense;
	}

	public Map<Long,Long> findExpenseSplitByExpenseId(Long expenseId){
		
		Map<Long,Long> map = new HashMap<Long,Long>();

		try {
			String sql = "SELECT * FROM expense_split WHERE expense_id = "+expenseId;
			ResultSet rs = dbUtil.executeSelectionQuery(sql);
			
			while(rs.next()){
			
				Long walletFrom = rs.getLong("wallet_id");
				Long walletAmount = rs.getLong("amount");
				map.put(walletFrom, walletAmount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CustomException("Expense not found.",404);
		}	
		return map;
	}

	public void deleteExpenseSplitByExpenseId(Long expenseId) {
		String sql = "DELETE FROM expense_split WHERE `expense_id` = "+expenseId;
		ResultSet rs = dbUtil.executeInsertionQuery(sql);
	}
	
	public void deleteById(Long id) {
		String sql = "DELETE FROM expenses WHERE `id` = "+id;
		ResultSet rs = dbUtil.executeInsertionQuery(sql);
	}



}
