import { Link, useNavigate } from "react-router-dom";

import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";

export interface SRPPaginationProps {
  resultsCount: number;
  page: number;
  query: string;
  setPage: (page: number) => void;
}

export default function SRPPagination({
  resultsCount,
  page,
  query,
  setPage,
}: SRPPaginationProps) {
  const navigate = useNavigate();

  const numberOfPages = Math.ceil(resultsCount / 20); // FIXME: Hardcoded 20 change later
  console.log("Number of pages", numberOfPages);

  let showPages = [
    page > 1 ? page - 1 : -1,
    page,
    page < numberOfPages ? page + 1 : numberOfPages,
  ];

  return (
    <div className="p-4">
      <Pagination>
        <PaginationContent>
          {page > 1 && (
            <PaginationItem onClick={() => setPage(page - 1)}>
              <Link to={`/search/${query}/${page - 1}`}>
                <PaginationPrevious />
              </Link>
            </PaginationItem>
          )}

          {showPages
            .filter((sPage) => sPage > 0)
            .map((sPage) => (
              <PaginationItem key={sPage} onClick={() => setPage(sPage)}>
                <Link to={`/search/${query}/${sPage}`}>
                  <PaginationLink isActive={sPage == page ? true : false}>
                    {sPage}
                  </PaginationLink>
                </Link>
              </PaginationItem>
            ))}

          <PaginationItem>
            <PaginationEllipsis />
          </PaginationItem>

          <PaginationItem onClick={() => setPage(page + 1)}>
            <Link to={`/search/${query}/${page + 1}`}>
              <PaginationNext />
            </Link>
          </PaginationItem>
        </PaginationContent>
      </Pagination>
    </div>
  );
}
