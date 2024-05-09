import ThemeIcon from "@/assets/icons/ThemeIcon";
import { ThemeContext } from "@/contexts/themeContext";
import { useContext, useState } from "react";

const themes = ["light", "dark", "rose", "black"];

function ThemeChanger() {
  const { theme, setTheme } = useContext(ThemeContext);
  const [dropdown, showDropdown] = useState(false);

  const changeTheme = (theme: string) => {
    setTheme(theme);
    showDropdown(false);
  };

  const dropdownMenu = () => (
    <div className="flex flex-col absolute w-96 nav-text bg-nav rounded-lg p-4 top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2">
      {themes.map((item) => (
        <div
          key={item}
          className={`cursor-pointer p-2 text-base border-b border-transparent hover:border-current border-b-1 ${
            theme === item ? "font-bold" : ""
          }`}
          onClick={() => changeTheme(item)}
        >
          {item}
        </div>
      ))}
    </div>
  );
  return (
    <div
      className="cursor-pointer fill-current"
      onClick={() => showDropdown(!dropdown)}
    >
      {dropdown && dropdownMenu()}
      <ThemeIcon className="inline mr-2" />
      {theme}
    </div>
  );
}

export default ThemeChanger;
