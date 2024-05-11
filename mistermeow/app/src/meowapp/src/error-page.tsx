import { useRouteError } from "react-router-dom";

export default function ErrorPage() {
  const error = useRouteError() as any;
  console.error(error);

  return (
    <div
      className="font-bilya min-h-dvh flex flex-col justify-center items-center text-rose-500 dark:text-rose-400
    "
      id="error-page"
    >
      <h1
        className="text-3xl mb-3
      "
      >
        Oops!
      </h1>
      <p>Sorry, an unexpected error has occurred.</p>
      <p className="text-rose-500">
        <i>{error.statusText || error.message}</i>
      </p>
    </div>
  );
}
