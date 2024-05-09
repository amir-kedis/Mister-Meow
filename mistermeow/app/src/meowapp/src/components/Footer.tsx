import ThemeChanger from "./ThemeChanger";
import VersionIcon from "../assets/icons/VersionIcon.tsx";

import { fetchTags } from "@/utils/external-api.tsx";
import { useQuery } from "react-query";

function Footer() {
  const { data, isLoading, isError } = useQuery("tags", fetchTags);
  return (
    <div className="text-nav bg-nav text-base p-3">
      <div className="container text-center flex justify-between align-center">
        <div className="font-bilya">EGYPT</div>
        <div className="font-jetbrains flex gap-4 ">
          <ThemeChanger />
          <a
            href="https://github.com/amir-kedis/Mister-Meow/"
            className="cursor-pointer"
          >
            <VersionIcon className="inline fill-current mr-2" />
            {isLoading
              ? "Loading..."
              : isError
              ? "Failed to fetch version"
              : "v" + data[0]?.name}
          </a>
        </div>
      </div>
    </div>
  );
}

export default Footer;
