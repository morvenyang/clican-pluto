//
//  RemoveCallbackURLJSONResponse.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RemoveCallbackURLJSONResponse : TTURLJSONResponse<TTURLResponse>{
    NSString* callbackName;
}

@property (nonatomic, copy)     NSString*       callbackName;

- (id)initWithCallbackName:(NSString*)callbackName;
@end
