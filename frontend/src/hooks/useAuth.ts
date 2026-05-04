import { useContext } from 'react';
import { AuthContext } from '@/context/AuthContext';

/**
 * useAuth hook - for components to use auth state
 * This is an alternative to using the context directly
 */
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export default useAuth;

