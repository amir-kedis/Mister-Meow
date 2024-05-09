import Logo from "@/assets/icons/logo";

function Banner() {
  return (
    <div>
      <div className="flex justify-center gap-3 place-items-center">
        <Logo size="lg" />
        <h1 className="text-5xl font-bilya font-bold tracking-widest max-w-[6ch] leading-normal">
          Mister Meow
        </h1>
      </div>
      <h2 className=" mt-4 text-lg text-center font-bilya font-bold tracking-widest">
        THE CURiOUS SEARCHER
      </h2>
    </div>
  );
}

export default Banner;
