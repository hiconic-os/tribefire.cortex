package tribefire.cortex;

public class PackagesLog {
	public static void main(String[] args) {
		for (var pkg: Package.getPackages()) {
			System.out.println(pkg);
		}
	}
}
