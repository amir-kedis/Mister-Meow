const { text } = require("stream/consumers");

/** @type {import('tailwindcss').Config} */
const generateColorClass = (variable) => {
  return ({ opacityValue }) => `var(--${variable})`;
};

const textColor = {
  mprimary: generateColorClass("primary-txt"),
  logo: generateColorClass("primary-txt"),
  logoCaption: generateColorClass("secondary-txt"),
  nav: generateColorClass("bottom-nav-txt"),
  btn: generateColorClass("btn-txt"),
  icon: generateColorClass("icon-color"),
  caption: generateColorClass("cap-text"),
  "sr-tag": generateColorClass("tag-primary"),
  "sr-host": generateColorClass("sr-host"),
  "sr-url": generateColorClass("sr-url"),
  "sr-title": generateColorClass("sr-title"),
  "sr-snippet": generateColorClass("sr-snippet"),
};

const backgroundColor = {
  home: generateColorClass("home-bg"),
  nav: generateColorClass("bottom-nav-bg"),
  btn: generateColorClass("btn-bg"),
  "nav-icon": generateColorClass("nav-icon-bg"),
  tie: generateColorClass("tie-color"),
  "sr-tag": generateColorClass("tag-secondary"),
  search: generateColorClass("search-bar-bg"),
};

const colors = {
  tie: generateColorClass("tie-color"),
  searchBorder: generateColorClass("search-bar-border"),
  searchShadow: generateColorClass("search-bar-shadow"),
  icon: generateColorClass("icon-color"),
  "sr-tag": generateColorClass("tag-primary"),
  border: "hsl(var(--border))",
  input: "hsl(var(--input))",
  ring: "hsl(var(--ring))",
  background: "hsl(var(--background))",
  foreground: "hsl(var(--foreground))",
  primary: {
    DEFAULT: "hsl(var(--primary))",
    foreground: "hsl(var(--primary-foreground))",
  },
  secondary: {
    DEFAULT: "hsl(var(--secondary))",
    foreground: "hsl(var(--secondary-foreground))",
  },
  destructive: {
    DEFAULT: "hsl(var(--destructive))",
    foreground: "hsl(var(--destructive-foreground))",
  },
  muted: {
    DEFAULT: "hsl(var(--muted))",
    foreground: "hsl(var(--muted-foreground))",
  },
  accent: {
    DEFAULT: "hsl(var(--accent))",
    foreground: "hsl(var(--accent-foreground))",
  },
  popover: {
    DEFAULT: "hsl(var(--popover))",
    foreground: "hsl(var(--popover-foreground))",
  },
  card: {
    DEFAULT: "hsl(var(--card))",
    foreground: "hsl(var(--card-foreground))",
  },
};

const fontFamily = {
  bilya: ["Bilya Layered", "sans-serif"],
  inter: ["Inter", "sans-serif"],
  jetbrains: ["JetBrains Mono", "monospace"],
};

const fillColors = textColor;

module.exports = {
  darkMode: ["class"],
  content: [
    "./pages/**/*.{ts,tsx}",
    "./components/**/*.{ts,tsx}",
    "./app/**/*.{ts,tsx}",
    "./src/**/*.{ts,tsx}",
  ],
  prefix: "",
  theme: {
    container: {
      center: true,
      padding: "2rem",
      screens: {
        "2xl": "1400px",
      },
    },
    extend: {
      textColor,
      backgroundColor,
      fontFamily,
      fillColors,
      colors,
    },
  },
  plugins: [require("tailwindcss-animate")],
};
