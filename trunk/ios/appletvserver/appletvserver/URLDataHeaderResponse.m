//
//  URLDataHeaderResponse.m
//  appletvserver
//
//  Created by zhang wei on 13-3-26.
//
//

#import "URLDataHeaderResponse.h"

@implementation URLDataHeaderResponse

@synthesize allHeaders = _allHeaders;

- (void)dealloc {
    TT_RELEASE_SAFELY(_allHeaders);
    
    [super dealloc];
}

- (NSError*)request:(TTURLRequest*)request processResponse:(NSHTTPURLResponse*)response
               data:(id)data {
    
    NSLog(@"status code:%i",response.statusCode);
    self.allHeaders = [NSDictionary dictionaryWithDictionary:[response allHeaderFields]];
    for(NSString* key in [self.allHeaders keyEnumerator]){
        NSLog(@"%@=%@",key,[self.allHeaders objectForKey:key]);
    }
    return [super request:request processResponse:response data:data];
}
@end
