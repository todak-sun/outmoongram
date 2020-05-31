package me.highdk.api.v1.common;

import java.util.Optional;

public interface OutmoonRepository<T, S>{
	
	public S save(S resource);
	
	public Optional<S> findById(T id);
	
}
