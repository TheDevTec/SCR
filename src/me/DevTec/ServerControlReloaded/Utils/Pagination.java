package me.DevTec.ServerControlReloaded.Utils;


import java.util.Arrays;
import java.util.List;
import java.util.Set;

import me.DevTec.TheAPI.Utils.DataKeeper.Collections.UnsortedList;

public class Pagination<T> extends UnsortedList<T> {

	private int pageSize;

	public Pagination(int pageSize) {
		this(pageSize, new UnsortedList<T>());
	}

	@SafeVarargs
	public Pagination(int pageSize, T... objects) {
		this(pageSize, Arrays.asList(objects));
	}

	public Pagination(int pageSize, List<T> objects) {
		this.pageSize = pageSize;
		addAll(objects);
	}

	public Pagination(int pageSize, Set<T> objects) {
		this.pageSize = pageSize;
		addAll(objects);
	}

	public int pageSize() {
		return pageSize;
	}

	public int totalPages() {
		return (int) Math.ceil((double) size() / pageSize);
	}

	public boolean exists(int page) {
		return !(page < 0) && page < totalPages();
	}

	public List<T> getPage(int page) {
		if (page < 0 || page >= totalPages())
			throw new IndexOutOfBoundsException("Index: " + page + ", Size: " + totalPages());

		int min = page * pageSize;
		int max = ((page * pageSize) + pageSize);

		if (max > size())
			max = size();

		return subList(min, max);
	}
}
