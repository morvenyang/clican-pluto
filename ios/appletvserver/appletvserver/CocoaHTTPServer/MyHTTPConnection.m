#include <ifaddrs.h>
#include <arpa/inet.h>
#import "MyHTTPConnection.h"
#import "HTTPMessage.h"
#import "HTTPDataResponse.h"
#import "DDNumber.h"
#import "HTTPLogging.h"

// Log levels : off, error, warn, info, verbose
// Other flags: trace
static const int httpLogLevel = HTTP_LOG_LEVEL_WARN; // | HTTP_LOG_FLAG_TRACE;


/**
 * All we have to do is override appropriate methods in HTTPConnection.
**/

@implementation MyHTTPConnection

- (NSString*) getIPAddress
{
    NSString *address = @"error";
    struct ifaddrs *interfaces = NULL;
    struct ifaddrs *temp_addr = NULL;
    int success = 0;
    
    // retrieve the current interfaces - returns 0 on success
    success = getifaddrs(&interfaces);
    if (success == 0) {
        // Loop through linked list of interfaces
        temp_addr = interfaces;
        while (temp_addr != NULL) {
            if( temp_addr->ifa_addr->sa_family == AF_INET) {
                // Check if interface is en0 which is the wifi connection on the iPhone
                if ([[NSString stringWithUTF8String:temp_addr->ifa_name] isEqualToString:@"en0"]) {
                    // Get NSString from C String
                    address = [NSString stringWithUTF8String:inet_ntoa(((struct sockaddr_in *)temp_addr->ifa_addr)->sin_addr)];
                }
            }
            
            temp_addr = temp_addr->ifa_next;
        }
    }
    
    // Free memory
    freeifaddrs(interfaces);
    
    return address;
}

- (NSObject<HTTPResponse> *)httpResponseForMethod:(NSString *)method URI:(NSString *)path
{
	HTTPLogTrace();
	
	if ([method isEqualToString:@"GET"] && ([path isEqualToString:@"/appletv/javascript/clican.js"]||[path isEqualToString:@"/appletv/releasenote.xml"]))
	{
		NSString  *replaceFilePath=[[[NSBundle mainBundle] bundlePath] stringByAppendingPathComponent:[@"web" stringByAppendingString:path]];
        NSString* replaceContent = [NSString stringWithContentsOfFile:replaceFilePath encoding:NSUTF8StringEncoding error:nil];
        NSString* ipAddress = [self getIPAddress];
        ipAddress = [ipAddress stringByAppendingString:@":8080"];
        NSLog(@"ip address=%@",ipAddress);
        replaceContent = [replaceContent stringByReplacingOccurrencesOfString:@"clican.org" withString:ipAddress];
		NSData *response = [replaceContent dataUsingEncoding:NSUTF8StringEncoding];
		return [[HTTPDataResponse alloc] initWithData:response];
	}else{
        return [super httpResponseForMethod:method URI:path];
    }
	
}

@end
