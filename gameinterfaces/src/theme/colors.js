// Neon UI Color Theme
export const neonColors = {
  // Primary Neon Colors
  primary: '#00E5FF',      // Electric Blue
  secondary: '#B500FF',    // Neon Purple
  accent: '#FF0099',       // Neon Pink
  
  // Extended Neon Palette
  cyan: '#00E5FF',
  magenta: '#FF00FF',
  lime: '#00FF00',
  orange: '#FF6600',
  
  // Backgrounds
  bg: {
    darkest: '#000000',
    dark: '#0a0e27',
    darker: '#1a1f3a',
    medium: '#2a2f4a',
  },
  
  // Text
  text: {
    primary: '#FFFFFF',
    secondary: '#E0E0E0',
    muted: '#A0A0A0',
  },
  
  // Status
  success: '#00FF41',
  warning: '#FFD700',
  error: '#FF0055',
  info: '#00E5FF',
};

export const neonTheme = {
  colors: neonColors,
  
  // Glow effects
  glow: {
    sm: `0 0 5px ${neonColors.primary}40, 0 0 10px ${neonColors.primary}20`,
    md: `0 0 10px ${neonColors.primary}60, 0 0 20px ${neonColors.primary}40, 0 0 30px ${neonColors.primary}20`,
    lg: `0 0 20px ${neonColors.primary}80, 0 0 40px ${neonColors.primary}60, 0 0 60px ${neonColors.primary}40`,
    
    cyan: `0 0 10px ${neonColors.cyan}60, 0 0 20px ${neonColors.cyan}40`,
    magenta: `0 0 10px ${neonColors.magenta}60, 0 0 20px ${neonColors.magenta}40`,
    pink: `0 0 10px ${neonColors.accent}60, 0 0 20px ${neonColors.accent}40`,
    purple: `0 0 10px ${neonColors.secondary}60, 0 0 20px ${neonColors.secondary}40`,
  },
  
  // Animations
  animation: {
    pulse: 'pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite',
    glow: 'glow 2s ease-in-out infinite',
    fadeIn: 'fadeIn 0.5s ease-in',
  },
};

export default neonTheme;
