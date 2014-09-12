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
    NSNumber* _alertGrade;
    NSNumber* _alertGrade_x;
    NSNumber* _alertGrade_y;
    NSNumber* _alertGrade_h;
    NSNumber* _dis_x;
    NSNumber* _dis_y;
    NSNumber* _dis_h;
    NSNumber* _d2;
    NSNumber* _d3;
}
@property (nonatomic, retain) NSNumber* v1;
@property (nonatomic, retain) NSNumber* v2;
@property (nonatomic, retain) NSNumber* v3;
@property (nonatomic, copy) NSString* type;
@property (nonatomic, retain) NSDate* dacTime;
@property (nonatomic, copy) NSString* pointName;
@property (nonatomic, retain) NSNumber* alertGrade;
@property (nonatomic, retain) NSNumber* alertGrade_x;
@property (nonatomic, retain) NSNumber* alertGrade_y;
@property (nonatomic, retain) NSNumber* alertGrade_h;
@property (nonatomic, retain) NSNumber* dis_x;
@property (nonatomic, retain) NSNumber* dis_y;
@property (nonatomic, retain) NSNumber* dis_h;
@property (nonatomic, retain) NSNumber* d2;
@property (nonatomic, retain) NSNumber* d3;
@end
