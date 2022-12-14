package api.v1.dao.wallets;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;

import api.v1.contexts.RequestContext;
import api.v1.entity.wallets.BankWallets;
import api.v1.exception.CustomException;
import api.v1.service.UsersService;
import api.v1.utils.DatabaseUtil;

public class BankWalletsDaoService {

	
	Gson gson;
	private static BankWalletsDaoService bankWalletsDaoService;
	DatabaseUtil dbUtil;
	UsersService usersDaoService;
	
	
	private BankWalletsDaoService() {

	}
	public static BankWalletsDaoService getInstance() {
		if (bankWalletsDaoService == null) {
			bankWalletsDaoService = new BankWalletsDaoService();
			bankWalletsDaoService.gson = new Gson();
			bankWalletsDaoService.dbUtil = DatabaseUtil.getInstance();
			bankWalletsDaoService.usersDaoService = UsersService.getInstance();
		}
		return bankWalletsDaoService;
	}

	
	public BankWallets  save(BankWallets  wallet,Long walletId) {
		// Wallet Creation
		String sql = "INSERT INTO `bank_wallet` (`note`,`wallet_id`) VALUES ('" + wallet.getNote() + "',"+walletId + ")";
		try {
			ResultSet rs = dbUtil.executeInsertionQuery(sql);
			rs.next();
			wallet.setId((long)rs.getInt(1));

		} catch (SQLException e) {
			System.err.println("Creation of bank wallet failed");
		}
		
		return wallet;
	}

	public BankWallets findById(long walletId){
		String query = "SELECT * FROM `bank_wallet` where wallet_id = " + walletId;
		ResultSet rs;
		BankWallets bankWallet;
		try {
			rs = dbUtil.executeSelectionQuery(query);
			rs.next();
			bankWallet = new BankWallets();
			bankWallet.setId(Long.parseLong(rs.getString("id")));
			bankWallet.setNote(rs.getString("note"));
		}catch(Exception e){
			e.printStackTrace();
			throw new CustomException("AN unexpected error occured",500,new Date().toLocaleString());
		}
		return bankWallet;
	}
	
	public void deleteById(long walletId) {
		String	sql = "DELETE FROM bank_wallet WHERE `wallet_id` = "+walletId;
		ResultSet rs = dbUtil.executeInsertionQuery(sql);
	}
	
	public void update(BankWallets wallet, Long walletId) {

		Long userId = (Long) ((ArrayList) RequestContext.getAttribute("pathKeys")).get(0);
		Long walletIdd = (Long) ((ArrayList)RequestContext.getAttribute("pathKeys")).get(1);
		
		// Wallet Updation
		
		String sql = "UPDATE `bank_wallet` SET `note`='"+wallet.getNote()+"' WHERE wallet_id="+walletIdd;
		int rs = dbUtil.executeUpdateQuery(sql);
		if(rs==0) throw new CustomException("Wallet is not found in your account or no changes made.",400,new Date().toLocaleString());
		
	}
}
