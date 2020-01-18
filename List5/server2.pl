use HTTP::Daemon;
use HTTP::Status;  
#use IO::File;

my $d = HTTP::Daemon->new(
         LocalAddr => 'localhost',
         LocalPort => 4321,
     )|| die;

print "Please contact me at: <URL:", $d->url, ">\n";


while (my $c = $d->accept) {
    while (my $request = $c->get_request) {
        if ($request->method eq 'GET') {
			$response = HTTP::Response->new();
			$response -> content($request->headers_as_string);
			$c->send_response($response);
        }
        else {
            $c->send_error(RC_FORBIDDEN)
        }

    }
    $c->close;
    undef($c);
}
