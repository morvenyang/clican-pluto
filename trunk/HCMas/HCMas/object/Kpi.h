//
//  Kpi.h
//  HCMas
//
//  Created by zhang wei on 14-9-6.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Kpi : NSObject{
    NSString* _type;
    NSNumber* _v1;
    NSNumber* _v2;
    NSNumber* _v3;
    NSDate* _dacTime;
    NSString* _pointName;
}
@property (nonatomic, retain) NSNumber* v1;
@property (nonatomic, retain) NSNumber* v2;
@property (nonatomic, retain) NSNumber* v3;
@property (nonatomic, copy) NSString* type;
@property (nonatomic, retain) NSDate* dacTime;
@property (nonatomic, copy) NSString* pointName;
@end
