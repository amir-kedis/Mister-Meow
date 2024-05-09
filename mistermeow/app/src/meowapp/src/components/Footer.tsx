import ThemeChanger from "./ThemeChanger";
import VersionIcon from "../assets/icons/VersionIcon.tsx";

function Footer() {
  return (
    <div className="text-nav bg-nav text-base p-3">
      <div className="container text-center flex justify-between align-center">
        <div className="font-bilya">EGYPT</div>
        <div className="font-jetbrains flex gap-4 ">
          <ThemeChanger />
          <span className="cursor-pointer">
            <VersionIcon className="inline mr-2" />
            v0.0.1
          </span>
        </div>
      </div>
    </div>
  );
}

export default Footer;
