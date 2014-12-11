//
//  Store.h
//  dailyreport
//
//  Created by zhang wei on 14-12-11.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Store : NSObject{
    NSString* _storeName;
    NSString* _storeCode;
}
@property (nonatomic, copy) NSString* storeName;
@property (nonatomic, copy) NSString* storeCode;
@end
