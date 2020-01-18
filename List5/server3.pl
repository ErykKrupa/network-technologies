use HTTP::Daemon;
use HTTP::Status;

my $d = HTTP::Daemon->new(
         LocalAddr => 'localhost',
         LocalPort => 4321,
     )|| die;

print "Please contact me at: <URL:", $d->url, ">\n";

while (my $c = $d->accept) {
    while (my $request = $c->get_request) {
        if ($request->method eq 'GET') {
			if ($request->uri->path eq "/") {
				$request->uri("/index1.html");
			}
			$c->send_file_response(".".$request->uri->path);
		}
        else {
            $c->send_error(RC_FORBIDDEN)
        }
    }
    $c->close;
    undef($c);
}
