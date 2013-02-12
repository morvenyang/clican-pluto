//
//  RemoveCallbackURLJSONResponse.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "RemoveCallbackURLJSONResponse.h"

@implementation RemoveCallbackURLJSONResponse

@synthesize rootObject  = _rootObject;
@synthesize callback = _callback;


- (id)initCallback:(NSString*)callback{
    if ((self = [super init])) {
        self.callback = callback;
    }
    return self;
}
///////////////////////////////////////////////////////////////////////////////////////////////////
- (void)dealloc {
    TT_RELEASE_SAFELY(_rootObject);
    
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
       
        json =  [json stringByTrimmingCharactersInSet:[NSCharacterSet newlineCharacterSet]]; 
        _rootObject = [[json JSONValue] retain];
        if (!_rootObject) {
            err = [NSError errorWithDomain:kTTExtJSONErrorDomain
                                      code:kTTExtJSONErrorCodeInvalidJSON
                                  userInfo:nil];
        }
    }
    
    return err;
}

@end
