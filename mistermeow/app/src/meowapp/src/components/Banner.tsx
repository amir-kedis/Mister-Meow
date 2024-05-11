import Logo from "@/assets/icons/logo";

export interface BannerProps {
  size?: string;
}

function Banner({ size }: BannerProps) {
  return (
    <div>
      <div className="flex justify-center gap-3 place-items-center">
        <Logo size={size} />
        <h1
          className={`text-${
            size == "sm" ? "base" : "5xl"
          } text-5xl font-bilya font-bold tracking-widest max-w-[6ch] leading-normal`}
        >
          Mister Meow
        </h1>
      </div>
      {size != "sm" && (
        <h2 className=" mt-4 text-lg text-center font-bilya font-bold tracking-widest">
          THE CURiOUS SEARCHER
        </h2>
      )}
    </div>
  );
}

export default Banner;
