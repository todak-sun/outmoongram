package me.highdk.api.v1.common;

import java.util.Optional;

public interface OutmoonRepository<T, Entity>{
	
	public Entity save(Entity resource);
	
	public Optional<Entity> findById(T id);
	
}
