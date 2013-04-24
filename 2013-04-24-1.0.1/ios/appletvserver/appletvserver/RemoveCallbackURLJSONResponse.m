//
//  RemoveCallbackURLJSONResponse.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "RemoveCallbackURLJSONResponse.h"

@implementation RemoveCallbackURLJSONResponse

@synthesize callbackName = _callbackName;


- (id)initWithCallbackName:(NSString*)cbn{
    if ((self = [super init])) {
        self.callbackName = cbn;
    }
    return self;
}
///////////////////////////////////////////////////////////////////////////////////////////////////
- (void)dealloc {
    TT_RELEASE_SAFELY(_callbackName);
    [super dealloc];
}


///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark -
#pragma mark TTURLResponse


///////////////////////////////////////////////////////////////////////////////////////////////////
- (NSError*)request:(TTURLRequest*)request processResponse:(NSHTTPURLResponse*)response
               data:(id)data {
    // This response is designed for NSData objects, so if we get anything else it's probably a
    // mistake.
    TTDASSERT([data isKindOfClass:[NSData class]]);
    TTDASSERT(nil == _rootObject);
    NSError* err = nil;
    if ([data isKindOfClass:[NSData class]]) {
        NSString* json = [[[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding] autorelease];
        NSLog(@"json=%@",json);
        json = [json stringByReplacingOccurrencesOfString:self.callbackName withString:@""];
        json = [json substringToIndex:[json length]-1];
        NSLog(@"json=%@",json);
        return [super request:request processResponse:response data:[json dataUsingEncoding:NSUTF8StringEncoding]];
    }
    
    return err;
}

@end
